import {
	HttpClient,
	HttpErrorResponse,
	HttpHeaders,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { CartItemSaveRequest } from '../../models/CartItemSaveRequest';
import { EventData } from '../..//models/EventData';
import { EventBusService } from '../event-bus/event-bus.service';
import { unauthorizedHandler } from '../auth/JwtAuthHttpInterceptor';
import { CartItemResponse } from 'src/app/models/CartItemResponse';
import { environment } from 'src/environments/environment';

const CART_SERVICE_URL = environment.apiUrl + '/cart';

const httpOptions = {
	headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
	providedIn: 'root',
})
export class CartService {
	constructor(
		private http: HttpClient,
		private eventBusService: EventBusService
	) {}

	public getCartSizeByUserId(userId: number): Observable<number> {
		return this.http
			.get<number>(CART_SERVICE_URL + `/${userId}/size`)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public addItemToCart(
		cartItemSaveRequest: CartItemSaveRequest
	): Observable<any> {
		return this.http
			.post(
				CART_SERVICE_URL + `/${cartItemSaveRequest.userId}/items`,
				cartItemSaveRequest,
				httpOptions
			)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public getCartItemsByUserId(userId: number): Observable<CartItemResponse[]> {
		return this.http
			.get<CartItemResponse[]>(CART_SERVICE_URL + `/${userId}/items`)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public dumpItemsFromCartByUserId(userId: number): Observable<any> {
		return this.http
			.delete(CART_SERVICE_URL + `/${userId}/items`)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public removeItemFromCart(userId: number, cartItemId: number): Observable<any> {
		return this.http
			.delete(CART_SERVICE_URL + `/${userId}/items/${cartItemId}`)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}
}
