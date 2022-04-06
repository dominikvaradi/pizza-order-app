import { Component, OnInit } from '@angular/core';
import { Address } from 'src/app/models/Address';
import { Order } from 'src/app/models/Order';
import { OrderWithInformationAndPizzas } from 'src/app/models/OrderWithInformationAndPizzas';
import { Pizza } from 'src/app/models/Pizza';

@Component({
	selector: 'app-order',
	templateUrl: './order.component.html',
	styleUrls: ['./order.component.css'],
})
export class OrderComponent implements OnInit {
	model: OrderWithInformationAndPizzas = new OrderWithInformationAndPizzas(
		new Order(
			1,
			'2022.03.29. 17:00',
			new Address(1, 1116, 'Budapest', 'Csabai kapu', '78'),
			6000,
			'Teljesítve'
		),
		[
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
		],
		'Teszt Elek Béla',
		202653353
	);

	constructor() {}

	ngOnInit(): void {}
}
