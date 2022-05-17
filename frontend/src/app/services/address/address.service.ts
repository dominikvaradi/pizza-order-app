import {
	HttpClient,
	HttpErrorResponse,
	HttpHeaders,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { AddressEditRequest } from 'src/app/models/AddressEditRequest';
import { AddressResponse } from 'src/app/models/AddressResponse';
import { environment } from 'src/environments/environment';
import { unauthorizedHandler } from '../auth/JwtAuthHttpInterceptor';
import { EventBusService } from '../event-bus/event-bus.service';

const ADDRESS_SERVICE_URL = environment.apiUrl + '/addresses';

const httpOptions = {
	headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
	providedIn: 'root',
})
export class AddressService {
	constructor(
		private http: HttpClient,
		private eventBusService: EventBusService
	) {}

	public editAddress(
		addressEditRequest: AddressEditRequest
	): Observable<AddressResponse> {
		return this.http
			.put<AddressResponse>(
				ADDRESS_SERVICE_URL + `/${addressEditRequest.id}`,
				addressEditRequest,
				httpOptions
			)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}

	public deleteAddress(addressId: number): Observable<any> {
		return this.http
			.delete(ADDRESS_SERVICE_URL + `/${addressId}`)
			.pipe(
				catchError((err: HttpErrorResponse) =>
					unauthorizedHandler(err, this.eventBusService)
				)
			);
	}
}
