import { Component, Input, OnInit } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormControl,
	FormGroup,
	Validators,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { AddressCreateRequest } from 'src/app/models/AddressCreateRequest';
import { AddressEditRequest } from 'src/app/models/AddressEditRequest';
import { AddressResponse } from 'src/app/models/AddressResponse';
import { AddressSaveResult } from 'src/app/models/AddressSaveResult';
import { AddressService } from 'src/app/services/address/address.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
	selector: 'app-save-address-modal',
	templateUrl: './save-address-modal.component.html',
	styleUrls: ['./save-address-modal.component.css'],
})
export class SaveAddressModalComponent implements OnInit {
	title: string = 'Cím módosítása';

	submitButtonTitle: string = 'Mentés';

	isLoading: boolean = false;

	@Input()
	address?: AddressResponse;

	@Input()
	userId?: number;

	saveAddressForm: FormGroup = this.formBuilder.group({
		zipCode: ['', [Validators.required, Validators.pattern('^[0-9]{4}$')]],
		city: ['', [Validators.required]],
		street: ['', [Validators.required]],
		houseNumber: ['', [Validators.required]],
	});

	get zipCode(): AbstractControl | null {
		return this.saveAddressForm.get('zipCode');
	}

	get city(): AbstractControl | null {
		return this.saveAddressForm.get('city');
	}

	get street(): AbstractControl | null {
		return this.saveAddressForm.get('street');
	}

	get houseNumber(): AbstractControl | null {
		return this.saveAddressForm.get('houseNumber');
	}

	constructor(
		private activeModal: NgbActiveModal,
		private formBuilder: FormBuilder,
		private userService: UserService,
		private addressService: AddressService
	) {}

	ngOnInit(): void {
		if (!this.address) {
			this.title = 'Cím létrehozása';
			this.submitButtonTitle = 'Létrehozás';

			return;
		}

		this.zipCode?.setValue(this.address.zipCode);
		this.city?.setValue(this.address.city);
		this.street?.setValue(this.address.street);
		this.houseNumber?.setValue(this.address.houseNumber);
	}

	onSubmit() {
		this.isLoading = true;
		let requestObservable: Observable<AddressResponse>;

		if (this.userId) {
			// New address creation
			requestObservable = this.userService.createAddress({
				zipCode: this.zipCode?.value,
				city: this.city?.value,
				street: this.street?.value,
				houseNumber: this.houseNumber?.value,
				userId: this.userId,
			});
		} else {
			// Address edit
			if (!this.address) {
				return;
			}

			requestObservable = this.addressService.editAddress({
				id: this.address.id,
				zipCode: this.zipCode?.value,
				city: this.city?.value,
				street: this.street?.value,
				houseNumber: this.houseNumber?.value,
			});
		}

		requestObservable.subscribe({
			next: (result) => {
				this.isLoading = false;
				this.activeModal.close(result);
			},
			error: (error) => {
				this.isLoading = false;
				this.activeModal.dismiss('Server-side error happened.');
			},
		});
	}

	closeModal() {
		this.activeModal.dismiss('Close click');
	}
}
