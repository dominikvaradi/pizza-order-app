import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CartService } from './services/cart/cart.service';
import { EventBusService } from './services/event-bus/event-bus.service';
import { UserDetailsStorageService } from './services/auth/user-details-storage.service';
import { UserResponse } from './models/UserResponse';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit, OnDestroy {
	title = 'Pizza-Order-App';

	isMenuCollapsed = true;

	cartItemCount: number = 0;
	loggedInUser: UserResponse | null = null;

	subscriptionList: Subscription[] = [];

	constructor(
		private router: Router,
		public route: ActivatedRoute,
		public userDetailsStorageService: UserDetailsStorageService,
		private cartService: CartService,
		private eventBusService: EventBusService
	) {}

	ngOnInit(): void {
		this.subscriptionList.push(
			this.eventBusService.on('logout', () => {
				this.logout();
			})
		);

		this.subscriptionList.push(
			this.eventBusService.on('cart-refresh-size', () => {
				this.getCartItemCount();
			})
		);

		this.subscriptionList.push(
			this.eventBusService.on('login', () => {
				this.loggedInUser = this.userDetailsStorageService.getUser();
				this.getCartItemCount();
			})
		);

		this.loggedInUser = this.userDetailsStorageService.getUser();
		this.getCartItemCount();
	}

	ngOnDestroy(): void {
		this.subscriptionList.forEach((subscription: Subscription) => {
			subscription.unsubscribe();
		});
	}

	toggleMenuCollapse() {
		this.isMenuCollapsed = !this.isMenuCollapsed;
	}

	collapseMenu() {
		this.isMenuCollapsed = true;
	}

	logout() {
		this.collapseMenu();
		this.userDetailsStorageService.logout();

		this.loggedInUser = null;
		this.cartItemCount = 0;

		this.router.navigate(['login']);
	}

	getCartItemCount(): void {
		if (!this.isUserLoggedIn() || !this.loggedInUser) {
			return;
		}

		this.subscriptionList.push(
			this.cartService.getCartSizeByUserId(this.loggedInUser.id).subscribe({
				next: (result: number) => {
					this.cartItemCount = result;
				},
				error: (error) => {
					this.cartItemCount = 0;
					return;
				},
			})
		);
	}

	isUserLoggedIn(): boolean {
		return this.userDetailsStorageService.isUserLoggedIn();
	}

	isUserChef(): boolean {
		return this.isUserLoggedIn() && this.loggedInUser?.role === 'ROLE_CHEF';
	}

	isUserManager(): boolean {
		return this.isUserLoggedIn() && this.loggedInUser?.role === 'ROLE_MANAGER';
	}

	isUserAdmin(): boolean {
		return this.isUserLoggedIn() && this.loggedInUser?.role === 'ROLE_ADMIN';
	}
}
