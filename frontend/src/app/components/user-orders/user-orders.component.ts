import { getLocaleExtraDayPeriods } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import {
	AddressResponse,
	addressToString,
} from 'src/app/models/AddressResponse';
import { OrderResponse } from 'src/app/models/OrderResponse';
import { UserResponse } from 'src/app/models/UserResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UserDetailsStorageService } from 'src/app/services/auth/user-details-storage.service';
import { EnumService } from 'src/app/services/enum/enum.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
	selector: 'app-user-orders',
	templateUrl: './user-orders.component.html',
	styleUrls: ['./user-orders.component.css'],
})
export class UserOrdersComponent implements OnInit, OnDestroy {
	orders: OrderResponse[] = [];
	page: number = 1;
	pageSize: number = 10;
	collectionSize: number = 0;

	detailedUser: UserResponse | null = null;
	activeOrder: OrderResponse | null = null;

	loggedInUser!: UserResponse;

	subscriptionList: Subscription[] = [];

	isOrdersLoading: boolean = true;
	isActiveOrderLoading: boolean = true;

	constructor(
		private userService: UserService,
		private router: Router,
		private activatedRoute: ActivatedRoute,
		private userDetailsStorageService: UserDetailsStorageService
	) {}

	ngOnInit(): void {
		const user = this.userDetailsStorageService.getUser();
		if (!this.userDetailsStorageService.isUserLoggedIn() || user === null) {
			this.router.navigate(['login']);
			return;
		}
		this.loggedInUser = user;

		this.loadPage();
	}

	ngOnDestroy(): void {
		this.subscriptionList.forEach((subscription) => subscription.unsubscribe());
	}

	private loadPage() {
		this.subscriptionList.push(
			this.activatedRoute.paramMap.subscribe((paramMap) => {
				const detailedUserId = parseInt(paramMap.get('userId') ?? '');

				if (isNaN(detailedUserId)) {
					this.router.navigate(['not-found']);
					return;
				}

				this.getDetailedUser(detailedUserId);
			})
		);
	}

	private getDetailedUser(userId: number) {
		this.subscriptionList.push(
			this.userService.getUserById(userId).subscribe({
				next: (result: UserResponse) => {
					this.detailedUser = result;
					this.refreshOrderPage();
					this.getUsersActiveOrder();
				},
				error: (error) => {
					// TODO
					this.isOrdersLoading = false;
					this.isActiveOrderLoading = false;

					console.log(JSON.stringify(error, null, 2));

					//this.router.navigate(['not-found']);
				},
			})
		);
	}

	private getUsersActiveOrder() {
		if (!this.detailedUser) {
			return;
		}

		this.isActiveOrderLoading = true;

		this.subscriptionList.push(
			this.userService
				.getActiveOrderByUserId(this.detailedUser.id, true)
				.subscribe({
					next: (result) => {
						this.activeOrder = result;
						this.isActiveOrderLoading = false;
					},
					error: (error) => {
						this.isActiveOrderLoading = false;
					},
				})
		);
	}

	private getOrders(page: number, pageSize: number) {
		if (!this.detailedUser) {
			return;
		}

		this.isOrdersLoading = true;

		this.subscriptionList.push(
			this.userService
				.getOrdersByUserIdPaginated(this.detailedUser.id, page - 1, pageSize)
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

	refreshOrderPage() {
		this.getOrders(this.page, this.pageSize);
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

	getStatusImage(name: string): string {
		switch (name) {
			case 'STATUS_UNDER_PROCESS':
				return 'assets/pizza-quarter.png';
			case 'STATUS_UNDER_PREPARATION':
				return 'assets/pizza-half.png';
			case 'STATUS_UNDER_DELIVER':
				return 'assets/pizza-quarter-to-full.png';
			default:
				return 'assets/pizza-full.png';
		}
	}
}
