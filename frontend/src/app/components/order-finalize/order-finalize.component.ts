import { Component, OnDestroy, OnInit } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormGroup,
	Validators,
} from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { AddressCreateRequest } from 'src/app/models/AddressCreateRequest';
import {
	AddressResponse,
	addressToString,
} from 'src/app/models/AddressResponse';
import { AddressSaveResult } from 'src/app/models/AddressSaveResult';
import { CartItemResponse } from 'src/app/models/CartItemResponse';
import { UserDetailsStorageService } from 'src/app/services/auth/user-details-storage.service';
import { CartService } from 'src/app/services/cart/cart.service';
import { OrderService } from 'src/app/services/order/order.service';
import { UserService } from 'src/app/services/user/user.service';
import { SaveAddressModalComponent } from '../save-address-modal/save-address-modal.component';
import { OrderCreateRequest } from '../../models/OrderCreateRequest';
import { UserResponse } from 'src/app/models/UserResponse';
import { Router } from '@angular/router';
import { PaymentMethodResponse } from 'src/app/models/PaymentMethodResponse';
import { EnumService } from 'src/app/services/enum/enum.service';

@Component({
	selector: 'app-order-finalize',
	templateUrl: './order-finalize.component.html',
	styleUrls: ['./order-finalize.component.css'],
})
export class OrderFinalizeComponent implements OnInit, OnDestroy {
	orderFinalizeForm: FormGroup = this.formBuilder.group({
		address: ['', [Validators.required]],
		paymentType: ['PAYMENT_METHOD_CASH', [Validators.required]],
	});

	loggedInUser!: UserResponse;
	addresses: AddressResponse[] = [];
	cartItems: CartItemResponse[] = [];
	hasActiveOrder: boolean = false;

	isActiveOrderLoading: boolean = true;
	isAddressesLoading: boolean = true;
	isItemsLoading: boolean = true;

	paymentMethods: PaymentMethodResponse[] = [];

	subscriptionList: Subscription[] = [];

	constructor(
		private modalService: NgbModal,
		private formBuilder: FormBuilder,
		private userService: UserService,
		private cartService: CartService,
		private orderService: OrderService,
		private enumService: EnumService,
		private userDetailsStorageService: UserDetailsStorageService,
		private router: Router
	) {}

	ngOnInit(): void {
		const user = this.userDetailsStorageService.getUser();
		if (!this.userDetailsStorageService.isUserLoggedIn() || user === null) {
			this.router.navigate(['login']);
			return;
		}
		this.loggedInUser = user;

		this.getActiveOrder();
		this.getCartItems();
		this.getAddresses();
		this.getPaymentMethods();
	}

	ngOnDestroy(): void {
		this.subscriptionList.forEach((subscription: Subscription) =>
			subscription.unsubscribe()
		);
	}

	get address(): AbstractControl | null {
		return this.orderFinalizeForm.get('address');
	}

	get paymentType(): AbstractControl | null {
		return this.orderFinalizeForm.get('paymentType');
	}

	private getActiveOrder() {
		this.isActiveOrderLoading = true;

		this.subscriptionList.push(
			this.userService.getActiveOrderByUserId(this.loggedInUser.id).subscribe({
				next: (result) => {
					this.hasActiveOrder = true;
					this.isActiveOrderLoading = false;
				},
				error: (error) => {
					this.hasActiveOrder = false;
					this.isActiveOrderLoading = false;
				},
			})
		);
	}

	private getAddresses() {
		this.isAddressesLoading = true;

		this.subscriptionList.push(
			this.userService.getAddressesByUserId(this.loggedInUser.id).subscribe({
				next: (result: AddressResponse[]) => {
					this.addresses = result;
					this.isAddressesLoading = false;
				},
				error: (error) => {
					this.addresses = [];
					this.isAddressesLoading = false;
				},
			})
		);
	}

	private getCartItems() {
		this.isItemsLoading = true;

		this.subscriptionList.push(
			this.cartService.getCartItemsByUserId(this.loggedInUser.id).subscribe({
				next: (result: CartItemResponse[]) => {
					this.cartItems = result;
					this.isItemsLoading = false;
				},
				error: (error) => {
					this.cartItems = [];
					this.isItemsLoading = false;
				},
			})
		);
	}

	private getPaymentMethods() {
		this.subscriptionList.push(
			this.enumService.getPaymentMethods().subscribe({
				next: (result) => {
					this.paymentMethods = result;
				},
			})
		);
	}

	openSaveAddressModal() {
		const modalRef = this.modalService.open(SaveAddressModalComponent, {
			centered: true,
		});

		modalRef.componentInstance.userId = this.loggedInUser.id;

		this.subscriptionList.push(
			modalRef.closed.subscribe({
				next: (result: AddressResponse) => {
					this.addresses.push(result);
				},
			})
		);
	}

	getReadablePaymentMethod(paymentMethodName: string): string {
		if (paymentMethodName === 'PAYMENT_METHOD_CASH') {
			return 'Készpénz';
		} else if (paymentMethodName === 'PAYMENT_METHOD_CREDIT_CARD') {
			return 'Bankkártya/Hitelkártya';
		}

		return 'Unknown payment method';
	}

	onSubmit() {
		const request: OrderCreateRequest = {
			userId: this.loggedInUser.id,
			addressId: this.address?.value.id,
			paymentMethodName: this.paymentType?.value,
		};

		this.createOrder(request);
	}

	private createOrder(order: OrderCreateRequest) {
		this.subscriptionList.push(
			this.orderService.createOrder(order).subscribe({
				next: (result) => {
					this.router.navigate(['/user', this.loggedInUser.id, 'orders']);
				},
				error: (error) => {
					this.router.navigate(['/']);
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

	addressToString(address: AddressResponse): string {
		return addressToString(address);
	}
}
