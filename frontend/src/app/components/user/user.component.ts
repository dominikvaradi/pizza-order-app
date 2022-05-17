import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faPenToSquare } from '@fortawesome/free-regular-svg-icons';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import {
	AddressResponse,
	addressToString,
} from 'src/app/models/AddressResponse';
import { RoleResponse } from 'src/app/models/RoleResponse';
import { UserEditRequest } from 'src/app/models/UserEditRequest';
import { UserResponse } from 'src/app/models/UserResponse';
import { AddressService } from 'src/app/services/address/address.service';
import { UserDetailsStorageService } from 'src/app/services/auth/user-details-storage.service';
import { EnumService } from 'src/app/services/enum/enum.service';
import { UserService } from 'src/app/services/user/user.service';
import CustomValidators from 'src/app/validation/validators';
import { SaveAddressModalComponent } from '../save-address-modal/save-address-modal.component';
import { SaveUserModalComponent } from '../save-user-modal/save-user-modal.component';

@Component({
	selector: 'app-user',
	templateUrl: './user.component.html',
	styleUrls: ['./user.component.css'],
})
export class UserComponent implements OnInit {
	customValidators: typeof CustomValidators = CustomValidators;

	roles: RoleResponse[] = [];

	detailedUser: UserResponse | null = null;
	addresses: AddressResponse[] = [];
	isUserLoading: boolean = true;
	isAddressesLoading: boolean = true;

	loggedInUser!: UserResponse;

	subscriptionList: Subscription[] = [];

	constructor(
		private modalService: NgbModal,
		iconLibrary: FaIconLibrary,
		private userService: UserService,
		private addressService: AddressService,
		private enumService: EnumService,
		private userDetailsStorageService: UserDetailsStorageService,
		private router: Router,
		private activatedRoute: ActivatedRoute
	) {
		iconLibrary.addIcons(faPenToSquare, faTrash);
	}

	ngOnInit(): void {
		const user = this.userDetailsStorageService.getUser();
		if (!this.userDetailsStorageService.isUserLoggedIn() || user === null) {
			this.router.navigate(['login']);
			return;
		}
		this.loggedInUser = user;

		this.loadPage();
	}

	ngOnDestroy(): void {
		this.subscriptionList.forEach((subscription) => subscription.unsubscribe());
	}

	private loadPage() {
		this.subscriptionList.push(
			this.activatedRoute.paramMap.subscribe((paramMap) => {
				if (!this.loggedInUser) return;

				const detailedUserId = parseInt(paramMap.get('userId') ?? '');

				if (isNaN(detailedUserId)) {
					this.router.navigate(['not-found']);
					return;
				}

				this.getDetailedUser(detailedUserId);
			})
		);
	}

	isLoggedInUserAdmin(): boolean {
		return this.loggedInUser.role === 'ROLE_ADMIN';
	}

	private getDetailedUser(userId: number) {
		this.subscriptionList.push(
			this.userService.getUserById(userId).subscribe({
				next: (result: UserResponse) => {
					this.detailedUser = result;
					this.isUserLoading = false;
					this.getDetailedUserAddresses();
					this.getRoles();
				},
				error: (error: HttpErrorResponse) => {
					this.isUserLoading = false;

					if (error.status === 404) {
						this.router.navigate(['not-found']);
					} else {
						// TODO többi hiba kezelése
						console.log(JSON.stringify(error, null, 2));
					}
				},
			})
		);
	}

	private getDetailedUserAddresses() {
		if (!this.detailedUser) {
			return;
		}

		this.subscriptionList.push(
			this.userService.getAddressesByUserId(this.detailedUser.id).subscribe({
				next: (result: AddressResponse[]) => {
					this.addresses = result;
					this.isAddressesLoading = false;
				},
				error: (error) => {
					this.addresses = [];
					this.isAddressesLoading = false;
				},
			})
		);
	}

	openEditValueModal(
		label: string,
		defaultValue: string,
		prefix: string | undefined = undefined,
		isNeedConfirm = false,
		isSecret = false
	) {
		if (!this.detailedUser) {
			return;
		}

		const modalRef = this.modalService.open(SaveUserModalComponent, {
			centered: true,
		});

		modalRef.componentInstance.defaultValue = defaultValue;
		modalRef.componentInstance.label = label;
		modalRef.componentInstance.prefix = prefix;
		modalRef.componentInstance.isNeedConfirm = isNeedConfirm;
		modalRef.componentInstance.isSecret = isSecret;

		switch (label) {
			case 'Felhasználónév':
				modalRef.componentInstance.validators = CustomValidators.usernameValidator;
				break;
			case 'Teljes Név':
				modalRef.componentInstance.validators = CustomValidators.fullNameValidator;
				break;
			case 'E-Mail cím':
				modalRef.componentInstance.validators = CustomValidators.emailValidator;
				break;
			case 'Telefonszám':
				modalRef.componentInstance.validators =
					CustomValidators.phoneNumberValidator;
				break;
			case 'Jelszó':
				modalRef.componentInstance.validators = CustomValidators.passwordValidator;
				break;
			default:
				modalRef.componentInstance.validators = [];
				break;
		}

		this.subscriptionList.push(
			modalRef.closed.subscribe({
				next: (value) => {
					if (!this.detailedUser) return;

					const request = this.getUserEditRequest(
						this.detailedUser.id,
						label,
						value
					);

					this.subscriptionList.push(
						this.userService.editUser(request).subscribe({
							next: (result: UserResponse) => {
								this.detailedUser = result;
							},
						})
					);
				},
			})
		);
	}

	private getUserEditRequest(
		userId: number,
		label: string,
		value: string
	): UserEditRequest {
		const request: UserEditRequest = {
			id: userId,
			username: undefined,
			password: undefined,
			email: undefined,
			phoneNumber: undefined,
			fullName: undefined,
			roleName: undefined,
		};

		switch (label) {
			case 'Felhasználónév':
				request.username = value;
				break;
			case 'Teljes Név':
				request.fullName = value;
				break;
			case 'E-Mail cím':
				request.email = value;
				break;
			case 'Telefonszám':
				request.phoneNumber = '+36' + value;
				break;
			case 'Jelszó':
				request.password = value;
				break;
			default:
				break;
		}

		return request;
	}

	private getRoles(): void {
		this.subscriptionList.push(
			this.enumService.getRoles().subscribe({
				next: (result) => {
					this.roles = result;
				},
			})
		);
	}

	openSaveAddressModal(index?: number) {
		if (!this.detailedUser) return;

		const modalRef = this.modalService.open(SaveAddressModalComponent, {
			centered: true,
		});

		if (typeof index !== 'undefined') {
			modalRef.componentInstance.address = this.addresses[index];
		} else {
			modalRef.componentInstance.userId = this.detailedUser.id;
		}

		this.subscriptionList.push(
			modalRef.closed.subscribe({
				next: (result: AddressResponse) => {
					if (typeof index !== 'undefined') {
						this.addresses[index] = result;
					} else {
						this.addresses.push(result);
					}
				},
			})
		);
	}

	deleteAddress(id: number) {
		this.subscriptionList.push(
			this.addressService.deleteAddress(id).subscribe({
				next: (result) => {
					this.addresses = this.addresses.filter((address) => address.id !== id);
				},
			})
		);
	}

	deleteUser() {
		if (!this.detailedUser) return;

		this.subscriptionList.push(
			this.userService.deleteUser(this.detailedUser.id).subscribe({
				next: (result) => {
					this.router.navigate(['/users']);
				},
			})
		);
	}

	changeRole(roleName: string) {
		if (!this.detailedUser) return;

		const request: UserEditRequest = {
			id: this.detailedUser.id,
			roleName: roleName,
		};

		this.subscriptionList.push(
			this.userService.editUser(request).subscribe({
				next: (result: UserResponse) => {
					this.detailedUser = result;
				},
			})
		);
	}

	addressToString(address: AddressResponse): string {
		return addressToString(address);
	}

	getRoleNameReadable(roleName: string): string {
		switch (roleName) {
			case 'ROLE_USER':
				return 'Felhasználó';
			case 'ROLE_MANAGER':
				return 'Menedzser';
			case 'ROLE_ADMIN':
				return 'Adminisztrátor';
			case 'ROLE_CHEF':
				return 'Szakács';
			default:
				return 'Ismeretlen';
		}
	}
}
