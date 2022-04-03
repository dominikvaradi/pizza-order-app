import { Component, OnInit } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormGroup,
	Validators,
} from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Address } from 'src/app/models/Address';
import { Pizza } from 'src/app/models/Pizza';
import { EditAddressModalComponent } from '../edit-address-modal/edit-address-modal.component';

@Component({
	selector: 'app-order-finalize',
	templateUrl: './order-finalize.component.html',
	styleUrls: ['./order-finalize.component.css'],
})
export class OrderFinalizeComponent implements OnInit {
	addresses: Address[];

	pizzas: Pizza[] = [
		new Pizza(
			1,
			'Margherita',
			'Paradicsomszósz, Mozzarella sajt, Paradicsom karika, Bazsalikom',
			'assets/margherita.jpg',
			2000
		),
		new Pizza(
			2,
			'Margherita',
			'Paradicsomszósz, Mozzarella sajt, Paradicsom karika, Bazsalikom',
			'assets/margherita.jpg',
			2000
		),
		new Pizza(
			3,
			'Margherita',
			'Paradicsomszósz, Mozzarella sajt, Paradicsom karika, Bazsalikom',
			'assets/margherita.jpg',
			2000
		),
	];

	orderFinalizeForm: FormGroup = this.formBuilder.group({
		address: ['', [Validators.required]],
		paymentType: ['cash', [Validators.required]],
	});

	get address(): AbstractControl | null {
		return this.orderFinalizeForm.get('address');
	}

	get paymentType(): AbstractControl | null {
		return this.orderFinalizeForm.get('paymentType');
	}

	constructor(private modalService: NgbModal, private formBuilder: FormBuilder) {
		this.addresses = [];

		for (let i: number = 1; i < 6; ++i) {
			this.addresses.push(new Address(i, 1116, 'Budapest', 'Csabai kapu', '78'));
		}
	}

	ngOnInit(): void {}

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

	onSubmit() {}

	sumOfPizzaPrices(): number {
		let sum: number = 0;
		this.pizzas.forEach((pizza) => {
			sum += pizza.price;
		});

		return sum;
	}
}
