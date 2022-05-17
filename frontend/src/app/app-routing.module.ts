import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ActiveOrdersComponent } from './components/active-orders/active-orders.component';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { MenuComponent } from './components/menu/menu.component';
import { OrderCartComponent } from './components/order-cart/order-cart.component';
import { OrderFinalizeComponent } from './components/order-finalize/order-finalize.component';
import { OrderComponent } from './components/order/order.component';
import { OrdersComponent } from './components/orders/orders.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { RegisterComponent } from './components/register/register.component';
import { UserOrdersComponent } from './components/user-orders/user-orders.component';
import { UserComponent } from './components/user/user.component';
import { UsersComponent } from './components/users/users.component';

const routes: Routes = [
	{ path: '', component: HomeComponent },
	{ path: 'login', component: LoginComponent },
	{ path: 'register', component: RegisterComponent },
	{ path: 'user/:userId/orders', component: UserOrdersComponent },
	{ path: 'order/:orderId', component: OrderComponent },
	{ path: 'user/:userId', component: UserComponent },
	{ path: 'order-cart', component: OrderCartComponent },
	{ path: 'order-finalize', component: OrderFinalizeComponent },
	{ path: 'orders', component: OrdersComponent },
	{ path: 'orders/active', component: ActiveOrdersComponent },
	{ path: 'menu', component: MenuComponent },
	{ path: 'users', component: UsersComponent },
	{ path: 'not-found', component: PageNotFoundComponent },
	{ path: '**', redirectTo: 'not-found' },
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule],
})
export class AppRoutingModule {}
