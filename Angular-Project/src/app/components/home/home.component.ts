import { Component, OnInit } from '@angular/core';
import { Pizza } from 'src/app/models/Pizza';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
	pizzas: Pizza[];

	constructor() {
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

	addToCart(pizzaId: number, amount: number) {
		/* TODO Service lekezelés */
		console.log(pizzaId + ': ' + amount);
	}

	parseNumber(numberString: string): number {
		let result: number = parseInt(numberString);

		if (isNaN(result) || result < 0) return 0;

		return result;
	}
}
