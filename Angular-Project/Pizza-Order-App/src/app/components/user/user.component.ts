import { Component, OnInit } from '@angular/core';
import { Validator, ValidatorFn, Validators } from '@angular/forms';
import {
	faPenToSquare,
	IconDefinition,
} from '@fortawesome/free-regular-svg-icons';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Address } from 'src/app/models/Address';
import { EditAddressModalComponent } from '../edit-address-modal/edit-address-modal.component';
import { EditUserModalComponent } from '../edit-user-modal/edit-user-modal.component';
import CustomValidators from 'src/app/validation/validators';
import { UserEditRequest } from 'src/app/models/UserEditRequest';
import { UserWithGroup } from 'src/app/models/UserWithGroup';

@Component({
	selector: 'app-user',
	templateUrl: './user.component.html',
	styleUrls: ['./user.component.css'],
})
export class UserComponent implements OnInit {
	model: UserWithGroup = new UserWithGroup(
		1,
		'TesztElek15',
		'Teszt Elek Béla',
		'tesztelek15@tesztmail.com',
		202653353,
		'Felhasználó'
	);

	addresses: Address[];

	customValidators: typeof CustomValidators = CustomValidators;

	// TODO
	groups: any[] = [
		{
			id: 1,
			name: 'Felhasználó',
		},
		{
			id: 2,
			name: 'Szakács',
		},
		{
			id: 3,
			name: 'Üzletvezető',
		},
		{
			id: 4,
			name: 'Admin',
		},
	];

	faPenToSquareIcon: IconDefinition = faPenToSquare;
	faTrashIcon: IconDefinition = faTrash;

	constructor(private modalService: NgbModal) {
		this.addresses = [];

		for (let i: number = 1; i < 6; ++i) {
			this.addresses.push(new Address(i, 1116, 'Budapest', 'Csabai kapu', '78'));
		}
	}

	ngOnInit(): void {}

	openEditUserModal(
		propertyName: string,
		valueLabel: string,
		initialValue: string = '',
		valueValidators: ValidatorFn[] = [],
		valueInputType = 'text',
		needsConfirm: boolean = false
	) {
		const modalRef = this.modalService.open(EditUserModalComponent, {
			centered: true,
		});

		modalRef.componentInstance.propertyName = propertyName;
		modalRef.componentInstance.valueLabel = valueLabel;
		modalRef.componentInstance.needsConfirm = needsConfirm;
		modalRef.componentInstance.initialValue = initialValue;
		modalRef.componentInstance.valueValidators = valueValidators;
		modalRef.componentInstance.valueInputType = valueInputType;

		modalRef.closed.subscribe({
			next: (value) => {
				const updatedModel = new UserEditRequest(
					this.model.id,
					this.model.username,
					this.model.fullName,
					this.model.email,
					this.model.phoneNumber,
					''
				);
				updatedModel.set(propertyName, value.value);

				// TODO Service feldolgozás, előtte akár password checking
				// Bekért jelszó ellenőrzéshez: value.password
				console.log(JSON.stringify(updatedModel));
			},
			error: (error) => {
				// TODO
				console.error(JSON.stringify(error));
			},
		});
	}

	openEditAddressModal(idx: number = NaN) {
		const modalRef = this.modalService.open(EditAddressModalComponent, {
			centered: true,
		});

		if (!isNaN(idx)) {
			modalRef.componentInstance.model = this.addresses[idx];
		}

		modalRef.closed.subscribe({
			next: (value) => {
				const addressModel = value as Address;

				// TODO Service feldolgozás
				console.log(JSON.stringify(addressModel));
			},
			error: (error) => {
				// TODO
				console.error(JSON.stringify(error));
			},
		});
	}

	deleteAddress(id: number) {
		// TODO Service feldolgozás
		console.log(id);
	}

	deleteUser() {
		// TODO Service feldolgozás
		console.log('Delete User');
	}

	changeGroup(id: number) {
		// TODO Service feldolgozás
		console.log('Change group, group id: ' + id);
	}
}
