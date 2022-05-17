import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { OrderStatusResponse } from 'src/app/models/OrderStatusResponse';
import { PaymentMethodResponse } from 'src/app/models/PaymentMethodResponse';
import { RoleResponse } from 'src/app/models/RoleResponse';
import { environment } from 'src/environments/environment';
import { unauthorizedHandler } from '../auth/JwtAuthHttpInterceptor';
import { EventBusService } from '../event-bus/event-bus.service';

const CART_SERVICE_URL = environment.apiUrl + '/enums';

@Injectable({
	providedIn: 'root',
})
export class EnumService {
	constructor(
		private http: HttpClient,
		private eventBusService: EventBusService
	) {}

	getOrderStatuses(): Observable<OrderStatusResponse[]> {
		return this.http
			.get<OrderStatusResponse[]>(CART_SERVICE_URL + '/order-statuses')
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	getPaymentMethods(): Observable<PaymentMethodResponse[]> {
		return this.http
			.get<PaymentMethodResponse[]>(CART_SERVICE_URL + '/payment-methods')
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	getRoles(): Observable<RoleResponse[]> {
		return this.http
			.get<RoleResponse[]>(CART_SERVICE_URL + '/roles')
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}
}
