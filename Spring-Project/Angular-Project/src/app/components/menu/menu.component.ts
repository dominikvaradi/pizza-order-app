import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Pizza } from 'src/app/models/Pizza';
import { PizzaEditRequest } from 'src/app/models/PizzaEditRequest';
import { EditPizzaModalComponent } from '../edit-pizza-modal/edit-pizza-modal.component';

@Component({
	selector: 'app-menu',
	templateUrl: './menu.component.html',
	styleUrls: ['./menu.component.css'],
})
export class MenuComponent implements OnInit {
	pizzas: Pizza[];

	constructor(private modalService: NgbModal) {
		this.pizzas = [];
		for (let i: number = 0; i < 10; ++i) {
			this.pizzas.push(
				new Pizza(
					i,
					'Margherita',
					'Paradicsomszósz, Mozzarella sajt, Paradicsom karika, Bazsalikom',
					'assets/margherita.jpg',
					2000
				)
			);
		}
	}

	ngOnInit(): void {}

	openEditPizzaModal(idx: number = NaN) {
		const modalRef = this.modalService.open(EditPizzaModalComponent, {
			centered: true,
		});

		if (!isNaN(idx)) {
			modalRef.componentInstance.model = this.pizzas[idx];
		}

		modalRef.closed.subscribe({
			next: (value) => {
				const pizzaRequestModel = value as PizzaEditRequest;

				// TODO Service feldolgozás
				console.log(JSON.stringify(pizzaRequestModel));
				console.log(pizzaRequestModel.image);
			},
			error: (error) => {
				// TODO
				console.error(JSON.stringify(error));
			},
		});
	}

	deletePizza(id: number) {
		// TODO Service feldolgozás
		console.log(id);
	}
}
