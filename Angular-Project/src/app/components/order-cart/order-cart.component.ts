import { Component, OnInit } from '@angular/core';
import { IconDefinition } from '@fortawesome/free-solid-svg-icons';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { Pizza } from 'src/app/models/Pizza';

@Component({
	selector: 'app-order-cart',
	templateUrl: './order-cart.component.html',
	styleUrls: ['./order-cart.component.css'],
})
export class OrderCartComponent implements OnInit {
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

	faTrashIcon: IconDefinition = faTrash;

	constructor() {}

	ngOnInit(): void {}

	sumOfPizzaPrices(): number {
		let sum: number = 0;
		this.pizzas.forEach((pizza) => {
			sum += pizza.price;
		});

		return sum;
	}

	removePizzaFromCart(pizzaId: number) {
		// TODO törlés service async
		console.log('removePizzaFromCart: ' + pizzaId);
	}

	dumpCart() {
		// TODO törlés service async
		console.log('dumpCart()');
	}
}
