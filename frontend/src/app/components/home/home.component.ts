import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { CartItemSaveRequest } from 'src/app/models/CartItemSaveRequest';
import { EventData } from 'src/app/models/EventData';
import { PizzaResponse } from 'src/app/models/PizzaResponse';
import { CartService } from 'src/app/services/cart/cart.service';
import { EventBusService } from 'src/app/services/event-bus/event-bus.service';
import {
	PizzaService,
	PIZZA_SERVICE_URL,
} from 'src/app/services/pizza/pizza.service';
import { UserDetailsStorageService } from 'src/app/services/auth/user-details-storage.service';
import { UserResponse } from 'src/app/models/UserResponse';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit, OnDestroy {
	pizzas: PizzaResponse[] = [];

	loggedInUser: UserResponse | null = null;

	subscriptionList: Subscription[] = [];

	isLoading: boolean = true;

	constructor(
		private pizzaService: PizzaService,
		private userDetailsStorageService: UserDetailsStorageService,
		private cartService: CartService,
		private eventBusService: EventBusService
	) {}

	ngOnDestroy(): void {
		this.subscriptionList.forEach((subscription) => subscription.unsubscribe());
	}

	ngOnInit(): void {
		this.getPizzas();

		this.loggedInUser = this.userDetailsStorageService.getUser();
	}

	private getPizzas(): void {
		this.isLoading = true;

		this.subscriptionList.push(
			this.pizzaService.getAllPizzas().subscribe({
				next: (result: PizzaResponse[]) => {
					this.pizzas = result;
					this.isLoading = false;
				},
				error: (error) => {
					this.pizzas = [];
					this.isLoading = false;
				},
			})
		);
	}

	getPizzaImageURLById(pizzaId: number): string {
		return PIZZA_SERVICE_URL + `/${pizzaId}/image`;
	}

	addToCart(pizzaId: number, amount: number) {
		if (!this.userDetailsStorageService.isUserLoggedIn() || !this.loggedInUser) return;

		const request: CartItemSaveRequest = {
			pizzaId: pizzaId,
			userId: this.loggedInUser.id,
			amount: amount,
		};

		this.cartService.addItemToCart(request).subscribe({
			next: (result) => {
				this.eventBusService.emit(new EventData('cart-refresh-size', null));
			},
		});
	}

	parseNumber(numberString: string): number {
		let result: number = parseInt(numberString);

		if (isNaN(result) || result < 0) return 0;

		return result;
	}
}
