import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AddressResponse, addressToString } from 'src/app/models/AddressResponse';
import { OrderResponse } from 'src/app/models/OrderResponse';
import { UserResponse } from 'src/app/models/UserResponse';
import { UserDetailsStorageService } from 'src/app/services/auth/user-details-storage.service';
import { OrderService } from 'src/app/services/order/order.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit, OnDestroy {
	orders: OrderResponse[] = [];
	page: number = 1;
	pageSize: number = 10;
	collectionSize: number = 0;
	term: string = "";

	loggedInUser!: UserResponse;

	subscriptionList: Subscription[] = [];

	isLoading: boolean = true;

	constructor(private orderService: OrderService,
		private router: Router,
		private userDetailsStorageService: UserDetailsStorageService) {}

	ngOnInit(): void {
		const user = this.userDetailsStorageService.getUser();
		if (!this.userDetailsStorageService.isUserLoggedIn() || user === null) {
			this.router.navigate(['login']);
			return;
		}
		this.loggedInUser = user;

		this.refreshOrderPage();
	}

	ngOnDestroy(): void {
		this.subscriptionList.forEach((subscription) => subscription.unsubscribe());
	}

	private getOrders(page: number, pageSize: number, term: string) {
		this.isLoading = true;

		this.subscriptionList.push(
			this.orderService
				.getOrdersPaginated(page - 1, pageSize, false, false, term)
				.subscribe({
					next: (result) => {
						this.orders = result.content;
						this.collectionSize = result.totalElements;
						this.isLoading = false;
					},
					error: (error) => {
						this.orders = [];
						this.isLoading = false;
					},
				})
		);
	}

	searchOrdersByUsername(searchTerm: string) {
		this.page = 1;
		this.term = searchTerm;
		this.refreshOrderPage();
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

	refreshOrderPage() {
		this.getOrders(this.page, this.pageSize, this.term);
	}
}
