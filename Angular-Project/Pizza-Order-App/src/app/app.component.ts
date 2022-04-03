import { Component } from '@angular/core';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css'],
})
export class AppComponent {
	title = 'Pizza-Order-App';

	isMenuCollapsed = true;

	collapseMenu() {
		this.isMenuCollapsed = true;
	}

	toggleCollapse() {
		this.isMenuCollapsed = !this.isMenuCollapsed;
	}
}
