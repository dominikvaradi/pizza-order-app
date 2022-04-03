import { Component, OnInit } from '@angular/core';
import { Address } from 'src/app/models/Address';
import { Order } from 'src/app/models/Order';

@Component({
	selector: 'app-user-orders',
	templateUrl: './user-orders.component.html',
	styleUrls: ['./user-orders.component.css'],
})
export class UserOrdersComponent implements OnInit {
	ORDERS!: Order[];

	page: number = 1;
	orderCountPerPage: number = 10;
	collectionSize!: number;
	visibleOrders!: Order[];

	constructor() {}

	ngOnInit(): void {
		this.ORDERS = [];
		for (let i: number = 1; i < 406; ++i) {
			this.ORDERS.push(this.createRandomOrder(i));
		}

		this.collectionSize = this.ORDERS.length;

		this.refreshOrders();
	}

	refreshOrders() {
		this.visibleOrders = this.ORDERS.slice(
			(this.page - 1) * this.orderCountPerPage,
			(this.page - 1) * this.orderCountPerPage + this.orderCountPerPage
		);
	}

	// TODO törölni, serviceből lekérdezni
	private createRandomOrder(id: number): Order {
		return new Order(
			id,
			'2022.' +
				('0' + Math.floor(((Math.random() * 100) % 12) + 1)).slice(-2) +
				'.' +
				('0' + Math.floor((Math.random() * 100) % 29) + 1).slice(-2) +
				'. ' +
				('0' + Math.floor((Math.random() * 100) % 24)).slice(-2) +
				':' +
				('0' + Math.floor((Math.random() * 100) % 60)).slice(-2),
			new Address(NaN, 1116, 'Budapest', 'Csabai kapu', '78'),
			Math.floor((Math.random() * 10000 * id) % 10000),
			'Teljesítve'
		);
	}
}
