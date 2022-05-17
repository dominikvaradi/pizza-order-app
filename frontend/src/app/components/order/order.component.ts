import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AddressResponse, addressToString } from 'src/app/models/AddressResponse';
import { OrderResponse } from 'src/app/models/OrderResponse';
import { OrderService } from 'src/app/services/order/order.service';

@Component({
	selector: 'app-order',
	templateUrl: './order.component.html',
	styleUrls: ['./order.component.css'],
})
export class OrderComponent implements OnInit {
	order: OrderResponse | null = null;

	subscriptionList: Subscription[] = [];

	isOrderLoading: boolean = true;

	constructor(
		private router: Router,
		private activatedRoute: ActivatedRoute,
		private orderService: OrderService
	) {}

	ngOnInit(): void {
		this.subscriptionList.push(
			this.activatedRoute.paramMap.subscribe((paramMap) => {
				const orderId = parseInt(paramMap.get('orderId') ?? '');

				if (isNaN(orderId)) {
					this.router.navigate(['not-found']);
					return;
				}

				this.getOrder(orderId);
			})
		);
	}

	getOrder(id: number) {
		this.subscriptionList.push(
			this.orderService
				.getOrderById(id, true)
				.subscribe({
					next: (result) => {
						this.order = result;
						this.isOrderLoading = false;
					},
					error: (error) => {
						this.isOrderLoading = false;
						this.router.navigate(['not-found']);
						return;
					},
				})
		);
	}

	getReadableOrderStatusName(name: string): string {
		switch (name) {
			case 'STATUS_UNDER_PROCESS':
				return 'Feldolgozás alatt';
			case 'STATUS_UNDER_PREPARATION':
				return 'Elkészítés alatt';
			case 'STATUS_UNDER_DELIVER':
				return 'Kiszállítás alatt';
			case 'STATUS_COMPLETED':
				return 'Teljesítve';
			case 'STATUS_REVOKED':
				return 'Visszavonva';
			default:
				return 'NOT_FOUND';
		}
	}

	addressToString(address: AddressResponse): string {
		return addressToString(address);
	}
}
