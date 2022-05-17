import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { PizzaResponse } from 'src/app/models/PizzaResponse';
import { UserResponse } from 'src/app/models/UserResponse';
import { UserDetailsStorageService } from 'src/app/services/auth/user-details-storage.service';
import {
	PizzaService,
	PIZZA_SERVICE_URL,
} from 'src/app/services/pizza/pizza.service';
import { SavePizzaModalComponent } from '../save-pizza-modal/save-pizza-modal.component';

@Component({
	selector: 'app-menu',
	templateUrl: './menu.component.html',
	styleUrls: ['./menu.component.css'],
})
export class MenuComponent implements OnInit {
	pizzas: PizzaResponse[] = [];

	loggedInUser: UserResponse | null = null;

	subscriptionList: Subscription[] = [];

	isLoading: boolean = true;

	constructor(
		private pizzaService: PizzaService,
		private userDetailsStorageService: UserDetailsStorageService,
		private modalService: NgbModal,
		private router: Router
	) {}

	ngOnInit(): void {
		const user = this.userDetailsStorageService.getUser();
		if (!this.userDetailsStorageService.isUserLoggedIn() || user === null) {
			this.router.navigate(['login']);
			return;
		}
		this.loggedInUser = user;

		this.getPizzas();
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

	openSavePizzaModal(index?: number) {
		const modalRef = this.modalService.open(SavePizzaModalComponent, {
			centered: true,
		});

		if (typeof index !== 'undefined') {
			modalRef.componentInstance.pizza = this.pizzas[index];
		}

		this.subscriptionList.push(
			modalRef.closed.subscribe({
				next: (result: PizzaResponse) => {
					if (typeof index !== 'undefined') {
						this.pizzas[index] = result;
					} else {
						this.pizzas.push(result);
					}
				},
			})
		);
	}

	getPizzaImageURLById(pizzaId: number): string {
		return PIZZA_SERVICE_URL + `/${pizzaId}/image`;
	}

	deletePizza(id: number) {
		this.subscriptionList.push(
			this.pizzaService.deletePizzaById(id).subscribe({
				next: (result) => {
					this.pizzas = this.pizzas.filter((pizza) => pizza.id !== id);
				},
			})
		);
	}
}
