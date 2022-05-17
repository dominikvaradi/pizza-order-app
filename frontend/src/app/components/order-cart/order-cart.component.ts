import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { Subscription } from 'rxjs';
import { CartItemResponse } from 'src/app/models/CartItemResponse';
import { EventData } from 'src/app/models/EventData';
import { UserResponse } from 'src/app/models/UserResponse';
import { UserDetailsStorageService } from 'src/app/services/auth/user-details-storage.service';
import { CartService } from 'src/app/services/cart/cart.service';
import { EventBusService } from 'src/app/services/event-bus/event-bus.service';

@Component({
	selector: 'app-order-cart',
	templateUrl: './order-cart.component.html',
	styleUrls: ['./order-cart.component.css'],
})
export class OrderCartComponent implements OnInit, OnDestroy {
	cartItems: CartItemResponse[] = [];

	subscriptionList: Subscription[] = [];

	loggedInUser!: UserResponse;

	isLoading: boolean = true;

	constructor(
		iconLibrary: FaIconLibrary,
		private cartService: CartService,
		private userDetailsStorageService: UserDetailsStorageService,
		private eventBusService: EventBusService,
		private router: Router
	) {
		iconLibrary.addIcons(faTrash);
	}

	ngOnDestroy(): void {
		this.subscriptionList.forEach((subscription) => subscription.unsubscribe());
	}

	ngOnInit(): void {
		const user = this.userDetailsStorageService.getUser();
		if (!this.userDetailsStorageService.isUserLoggedIn() || user === null) {
			this.router.navigate(['login']);
			return;
		}
		this.loggedInUser = user;

		this.getCartItems();
	}

	private getCartItems() {
		this.isLoading = true;
		
		this.subscriptionList.push(
			this.cartService.getCartItemsByUserId(this.loggedInUser.id).subscribe({
				next: (result: CartItemResponse[]) => {
					this.cartItems = result;
					this.isLoading = false;
				},
				error: (error) => {
					this.cartItems = [];
					this.isLoading = false;
				},
			})
		);
	}

	removePizzaFromCart(cartItemId: number) {
		this.subscriptionList.push(
			this.cartService
				.removeItemFromCart(this.loggedInUser.id, cartItemId)
				.subscribe({
					next: (result) => {
						this.cartItems = this.cartItems.filter(
							(cartItem) => cartItem.id != cartItemId
						);
						this.eventBusService.emit(new EventData('cart-refresh-size', null));
					},
				})
		);
	}

	dumpCart() {
		this.subscriptionList.push(
			this.cartService.dumpItemsFromCartByUserId(this.loggedInUser.id).subscribe({
				next: (result) => {
					this.cartItems = [];
					this.eventBusService.emit(new EventData('cart-refresh-size', null));
				},
			})
		);
	}

	sumOfPizzaPrices(): number {
		let sum: number = 0;

		this.cartItems.forEach((cartItem) => {
			sum += cartItem.pizza.price * cartItem.amount;
		});

		return sum;
	}
}
