import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import {
	AddressResponse,
	addressToString,
} from 'src/app/models/AddressResponse';
import { OrderResponse } from 'src/app/models/OrderResponse';
import { OrderStatusEditRequest } from 'src/app/models/OrderStatusEditRequest';
import { OrderStatusResponse } from 'src/app/models/OrderStatusResponse';
import { UserResponse } from 'src/app/models/UserResponse';
import { UserDetailsStorageService } from 'src/app/services/auth/user-details-storage.service';
import { EnumService } from 'src/app/services/enum/enum.service';
import { OrderService } from 'src/app/services/order/order.service';

@Component({
	selector: 'app-active-orders',
	templateUrl: './active-orders.component.html',
	styleUrls: ['./active-orders.component.css'],
})
export class ActiveOrdersComponent implements OnInit, OnDestroy {
	orders: OrderResponse[] = [];
	page: number = 1;
	pageSize: number = 10;
	collectionSize: number = 0;

	loggedInUser!: UserResponse;

	subscriptionList: Subscription[] = [];

	isOrdersLoading: boolean = true;

	orderStatuses: OrderStatusResponse[] = [];

	constructor(
		private router: Router,
		private orderService: OrderService,
		private enumService: EnumService,
		private userDetailsStorageService: UserDetailsStorageService
	) {}

	ngOnInit(): void {
		const user = this.userDetailsStorageService.getUser();
		if (!this.userDetailsStorageService.isUserLoggedIn() || user === null) {
			this.router.navigate(['login']);
			return;
		}
		this.loggedInUser = user;

		this.getOrderStatuses();
		this.refreshOrderPage();
	}

	ngOnDestroy(): void {
		this.subscriptionList.forEach((subscription) => subscription.unsubscribe());
	}

	private getOrders(page: number, pageSize: number) {
		this.isOrdersLoading = true;

		this.subscriptionList.push(
			this.orderService
				.getOrdersPaginated(page - 1, pageSize, true, true)
				.subscribe({
					next: (result) => {
						this.orders = result.content;
						this.collectionSize = result.totalElements;
						this.isOrdersLoading = false;
					},
					error: (error) => {
						this.orders = [];
						this.isOrdersLoading = false;
					},
				})
		);
	}

	private getOrderStatuses(): void {
		this.subscriptionList.push(
			this.enumService.getOrderStatuses().subscribe({
				next: (result) => {
					this.orderStatuses = result;
				},
			})
		);
	}

	editStatus(index: number, orderStatusId: number) {
		const orderToEdit = this.orders[index];

		const request: OrderStatusEditRequest = {
			id: orderToEdit.id,
			orderStatusId: orderStatusId,
		};

		this.subscriptionList.push(
			this.orderService.editOrderStatus(request).subscribe({
				next: (result) => {
					if (this.isOrderStatusNameIsActive(result.orderStatusName))
						orderToEdit.orderStatusName = result.orderStatusName;
					else
						this.orders = this.orders.filter((order) => order.id !== orderToEdit.id);
				},
			})
		);
	}

	refreshOrderPage() {
		this.getOrders(this.page, this.pageSize);
	}

	getElapsedMinutes(elapsedFrom: Date): number {
		const fromObj: Date = new Date(elapsedFrom);
		const now: Date = new Date();

		const elapsedMinutes = Math.floor(
			(now.getTime() - fromObj.getTime()) / 1000 / 60
		);

		return elapsedMinutes;
	}

	addressToString(address: AddressResponse): string {
		return addressToString(address);
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

	private isOrderStatusNameIsActive(name: string) {
		if (name === 'STATUS_COMPLETED' || name === 'STATUS_REVOKED') return false;

		return true;
	}
}
