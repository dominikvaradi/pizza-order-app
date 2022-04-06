import { Component, OnInit } from '@angular/core';
import { Address } from 'src/app/models/Address';
import { Order } from 'src/app/models/Order';
import { OrderWithInformationAndPizzas } from 'src/app/models/OrderWithInformationAndPizzas';
import { Pizza } from 'src/app/models/Pizza';

@Component({
	selector: 'app-active-orders',
	templateUrl: './active-orders.component.html',
	styleUrls: ['./active-orders.component.css'],
})
export class ActiveOrdersComponent implements OnInit {
	activeOrders: OrderWithInformationAndPizzas[];

	constructor() {
		this.activeOrders = [];

		for (let i = 1; i < 20; ++i) {
			const pizzasForOrder: Pizza[] = [];
			for (let j = 0; j < (Math.random() * 10 * i) % 8; ++j) {
				pizzasForOrder.push(
					new Pizza(
						i,
						'Margherita',
						'Paradicsomszósz, Mozzarella sajt, Paradicsom karika, Bazsalikom',
						'assets/margherita.jpg',
						2000
					)
				);
			}

			this.activeOrders.push(
				new OrderWithInformationAndPizzas(
					this.createRandomOrder(i),
					pizzasForOrder,
					'Teszt Elek',
					202663333
				)
			);
		}
	}

	ngOnInit(): void {}

	// TODO törölni, serviceből lekérdezni
	private createRandomOrder(id: number): Order {
		const currentDate: Date = new Date();

		return new Order(
			id,
			'2022.' +
				('0' + (currentDate.getMonth() + 1)).slice(-2) +
				'.' +
				('0' + currentDate.getDate()).slice(-2) +
				'. ' +
				('0' + currentDate.getHours()).slice(-2) +
				':' +
				('0' + Math.floor((Math.random() * 100) % currentDate.getMinutes())).slice(
					-2
				),
			new Address(NaN, 1116, 'Budapest', 'Csabai kapu', '78'),
			Math.floor((Math.random() * 10000 * id) % 10000),
			'Teljesítve'
		);
	}

	getElapsedTime(from: string): string {
		const currentDate: Date = new Date();

		const orderDate: Date = new Date(from);

		const elapsedYear = currentDate.getUTCFullYear() - orderDate.getUTCFullYear();
		const elapsedMonth = currentDate.getUTCMonth() - orderDate.getUTCMonth();
		const elapsedDays = currentDate.getUTCDate() - orderDate.getUTCDate();
		const elapsedHours = currentDate.getUTCHours() - orderDate.getUTCHours();
		const elapsedMinutes =
			currentDate.getUTCMinutes() - orderDate.getUTCMinutes();

		return (
			elapsedMinutes +
			elapsedHours * 60 +
			elapsedDays * 24 * 60 +
			elapsedMonth * 30 * 24 * 60 +
			elapsedYear * 12 * 30 * 24 * 60
		).toString();
	}
}
