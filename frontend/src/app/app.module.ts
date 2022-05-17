import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';

import { jwtAuthHttpInterceptorProviders } from './services/auth/JwtAuthHttpInterceptor';

import { AppRoutingModule } from './app-routing.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AutosizeModule } from 'ngx-autosize';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { OrderCartComponent } from './components/order-cart/order-cart.component';
import { OrderFinalizeComponent } from './components/order-finalize/order-finalize.component';
import { UserComponent } from './components/user/user.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { SaveUserModalComponent } from './components/save-user-modal/save-user-modal.component';
import { SaveAddressModalComponent } from './components/save-address-modal/save-address-modal.component';
import { UserOrdersComponent } from './components/user-orders/user-orders.component';
import { RegisterComponent } from './components/register/register.component';
import { ActiveOrdersComponent } from './components/active-orders/active-orders.component';
import { OrdersComponent } from './components/orders/orders.component';
import { MenuComponent } from './components/menu/menu.component';
import { SavePizzaModalComponent } from './components/save-pizza-modal/save-pizza-modal.component';
import { UsersComponent } from './components/users/users.component';
import { OrderComponent } from './components/order/order.component';

@NgModule({
	declarations: [
		AppComponent,
		PageNotFoundComponent,
		HomeComponent,
		LoginComponent,
		RegisterComponent,
		UserOrdersComponent,
		SaveAddressModalComponent,
		OrderComponent,
		UserComponent,
		SaveUserModalComponent,
		OrderCartComponent,
		OrderFinalizeComponent,
		OrdersComponent,
		ActiveOrdersComponent,
		SavePizzaModalComponent,
		MenuComponent,
		UsersComponent,
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		ReactiveFormsModule,
		HttpClientModule,
		NgbModule,
		FontAwesomeModule,
		AutosizeModule,
	],
	providers: [jwtAuthHttpInterceptorProviders],
	bootstrap: [AppComponent],
})
export class AppModule {}
