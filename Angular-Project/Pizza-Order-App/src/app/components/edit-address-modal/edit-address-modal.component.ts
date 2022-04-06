import { Component, Input, OnInit } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormControl,
	FormGroup,
	Validators,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Address } from 'src/app/models/Address';

@Component({
	selector: 'app-edit-address-modal',
	templateUrl: './edit-address-modal.component.html',
	styleUrls: ['./edit-address-modal.component.css'],
})
export class EditAddressModalComponent implements OnInit {
	@Input()
	model: Address | undefined;

	title: string = 'Cím módosítása';
	
	submitButtonTitle: string = 'Mentés';

	editAddressForm: FormGroup = this.formBuilder.group({
		zipCode: ['', [Validators.required, Validators.pattern('^[0-9]{4}$')]],
		city: ['', [Validators.required]],
		street: ['', [Validators.required]],
		number: ['', [Validators.required]],
	});

	get zipCode(): AbstractControl | null {
		return this.editAddressForm.get('zipCode');
	}

	get city(): AbstractControl | null {
		return this.editAddressForm.get('city');
	}

	get street(): AbstractControl | null {
		return this.editAddressForm.get('street');
	}

	get number(): AbstractControl | null {
		return this.editAddressForm.get('number');
	}

	constructor(
		private activeModal: NgbActiveModal,
		private formBuilder: FormBuilder
	) {}

	ngOnInit(): void {
		if (!this.model) {
			this.title = 'Cím létrehozása';
			this.submitButtonTitle = 'Létrehozás';
		}

		this.zipCode?.setValue(this.model?.zipCode);
		this.city?.setValue(this.model?.city);
		this.street?.setValue(this.model?.street);
		this.number?.setValue(this.model?.number);
	}

	onSubmit() {
		this.model = new Address(
			this.model?.id ?? NaN,
			this.zipCode?.value,
			this.city?.value,
			this.street?.value,
			this.number?.value
		);

		this.activeModal.close(this.model);
	}

	closeModal() {
		this.activeModal.dismiss('Close click');
	}
}
