import { Component, Input, OnInit } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormGroup,
	Validators,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { PizzaCreateRequest } from 'src/app/models/PizzaCreateRequest';
import { PizzaEditRequest } from 'src/app/models/PizzaEditRequest';
import { PizzaResponse } from 'src/app/models/PizzaResponse';
import { PizzaService } from 'src/app/services/pizza/pizza.service';

@Component({
	selector: 'app-save-pizza-modal',
	templateUrl: './save-pizza-modal.component.html',
	styleUrls: ['./save-pizza-modal.component.css'],
})
export class SavePizzaModalComponent implements OnInit {
	title: string = 'Pizza módosítása';

	submitButtonTitle: string = 'Mentés';

	isLoading: boolean = false;

	@Input()
	pizza?: PizzaResponse;

	savePizzaForm: FormGroup = this.formBuilder.group({
		name: ['', [Validators.required]],
		description: [''],
		price: ['', [Validators.required, Validators.pattern('^[1-9][0-9]*$')]],
	});

	file?: File;

	constructor(
		private activeModal: NgbActiveModal,
		private formBuilder: FormBuilder,
		private pizzaService: PizzaService
	) {}

	ngOnInit(): void {
		if (!this.pizza) {
			this.title = 'Pizza létrehozása';
			this.submitButtonTitle = 'Létrehozás';

			this.image?.addValidators(Validators.required);
			this.imageSourceFile?.addValidators(Validators.required);

			return;
		}

		this.name?.setValue(this.pizza.name);
		this.description?.setValue(this.pizza.description);
		this.price?.setValue(this.pizza.price);
	}

	onSubmit() {
		this.isLoading = true;
		let requestObservable: Observable<PizzaResponse>;

		if (this.pizza) { // Pizza edit
			const price = parseInt(this.price?.value);
			const request: PizzaEditRequest = {
				id: this.pizza.id,
				name: this.name?.value,
				description: this.description?.value,
				price: isNaN(price) ? undefined : price
			};

			requestObservable = this.pizzaService.editPizza(request, this.file);
		} else { // Pizza creation
			if (!this.file) return;

			const price = parseInt(this.price?.value);
			const request: PizzaCreateRequest = {
				name: this.name?.value,
				description: this.description?.value,
				price: isNaN(price) ? 0 : price
			};

			requestObservable = this.pizzaService.createPizza(request, this.file);
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

	onFileChange(event: any) {
		if (event.target.files.length > 0) {
			this.file = event.target.files[0];
		}
	}

	get name(): AbstractControl | null {
		return this.savePizzaForm.get('name');
	}

	get description(): AbstractControl | null {
		return this.savePizzaForm.get('description');
	}

	get image(): AbstractControl | null {
		return this.savePizzaForm.get('image');
	}

	get imageSourceFile(): AbstractControl | null {
		return this.savePizzaForm.get('imageSourceFile');
	}

	get price(): AbstractControl | null {
		return this.savePizzaForm.get('price');
	}
}
