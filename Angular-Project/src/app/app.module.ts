import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { UserOrdersComponent } from './components/user-orders/user-orders.component';
import { EditAddressModalComponent } from './components/edit-address-modal/edit-address-modal.component';
import { OrderComponent } from './components/order/order.component';
import { UserComponent } from './components/user/user.component';
import { EditUserModalComponent } from './components/edit-user-modal/edit-user-modal.component';
import { OrderCartComponent } from './components/order-cart/order-cart.component';
import { OrderFinalizeComponent } from './components/order-finalize/order-finalize.component';
import { OrdersComponent } from './components/orders/orders.component';
import { EditPizzaModalComponent } from './components/edit-pizza-modal/edit-pizza-modal.component';
import { MenuComponent } from './components/menu/menu.component';
import { AutosizeModule } from 'ngx-autosize';
import { UsersComponent } from './components/users/users.component';
import { ActiveOrdersComponent } from './components/active-orders/active-orders.component';

@NgModule({
	declarations: [
		AppComponent,
		PageNotFoundComponent,
		HomeComponent,
		HomeComponent,
		LoginComponent,
		RegisterComponent,
		UserOrdersComponent,
		EditAddressModalComponent,
		OrderComponent,
		UserComponent,
		EditUserModalComponent,
		OrderCartComponent,
		OrderFinalizeComponent,
		OrdersComponent,
		EditPizzaModalComponent,
		MenuComponent,
  UsersComponent,
  ActiveOrdersComponent,
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		ReactiveFormsModule,
		NgbModule,
		FontAwesomeModule,
		AutosizeModule,
	],
	providers: [],
	bootstrap: [AppComponent],
})
export class AppModule {}
