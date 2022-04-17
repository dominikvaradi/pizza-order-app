import { Component, Input, OnInit } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormGroup,
	Validators,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Pizza } from 'src/app/models/Pizza';
import { PizzaEditRequest } from 'src/app/models/PizzaEditRequest';

@Component({
	selector: 'app-edit-pizza-modal',
	templateUrl: './edit-pizza-modal.component.html',
	styleUrls: ['./edit-pizza-modal.component.css'],
})
export class EditPizzaModalComponent implements OnInit {
	@Input()
	model: Pizza | undefined;

	title: string = 'Pizza módosítása';

	submitButtonTitle: string = 'Mentés';

	editPizzaForm: FormGroup = this.formBuilder.group({
		name: ['', [Validators.required]],
		description: ['', [Validators.required]],
		image: ['', []],
		imageSourceFile: [null, []],
		price: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
	});

	get name(): AbstractControl | null {
		return this.editPizzaForm.get('name');
	}

	get description(): AbstractControl | null {
		return this.editPizzaForm.get('description');
	}

	get image(): AbstractControl | null {
		return this.editPizzaForm.get('image');
	}

	get imageSourceFile(): AbstractControl | null {
		return this.editPizzaForm.get('imageSourceFile');
	}

	get price(): AbstractControl | null {
		return this.editPizzaForm.get('price');
	}

	constructor(
		private activeModal: NgbActiveModal,
		private formBuilder: FormBuilder
	) {}

	ngOnInit(): void {
		if (!this.model) {
			this.title = 'Pizza létrehozása';
			this.submitButtonTitle = 'Létrehozás';

			this.image?.addValidators(Validators.required);
			this.imageSourceFile?.addValidators(Validators.required);
		}

		this.name?.setValue(this.model?.name);
		this.description?.setValue(this.model?.description);
		this.price?.setValue(this.model?.price);
	}

	onSubmit() {
		const newModel = new PizzaEditRequest(
			this.model?.id ?? NaN,
			this.name?.value,
			this.description?.value,
			this.imageSourceFile?.value,
			this.price?.value
		);

		this.activeModal.close(newModel);
	}

	closeModal() {
		this.activeModal.dismiss('Close click');
	}

	onFileChange(event: any) {
		if (event.target.files.length > 0) {
			const file = event.target.files[0];

			this.editPizzaForm.patchValue({
				imageSourceFile: file,
			});
		}
	}
}
