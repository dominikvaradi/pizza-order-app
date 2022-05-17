import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserResponse } from 'src/app/models/UserResponse';
import { UserDetailsStorageService } from 'src/app/services/auth/user-details-storage.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
	selector: 'app-users',
	templateUrl: './users.component.html',
	styleUrls: ['./users.component.css'],
})
export class UsersComponent implements OnInit, OnDestroy {
	users: UserResponse[] = [];
	page: number = 1;
	pageSize: number = 10;
	collectionSize: number = 0;
	term: string = '';

	loggedInUser!: UserResponse;

	subscriptionList: Subscription[] = [];

	isLoading: boolean = true;

	constructor(
		private userService: UserService,
		private router: Router,
		private userDetailsStorageService: UserDetailsStorageService
	) {}

	ngOnInit(): void {
		const user = this.userDetailsStorageService.getUser();
		if (!this.userDetailsStorageService.isUserLoggedIn() || user === null) {
			this.router.navigate(['login']);
			return;
		}
		this.loggedInUser = user;

		this.refreshUsers();
	}

	ngOnDestroy(): void {
		this.subscriptionList.forEach((subscription) => subscription.unsubscribe());
	}

	private getUsers(page: number, pageSize: number, term: string) {
		this.isLoading = true;

		this.subscriptionList.push(
			this.userService.getUsersPaginated(page - 1, pageSize, term).subscribe({
				next: (result) => {
					this.users = result.content;
					this.collectionSize = result.totalElements;
					this.isLoading = false;
				},
				error: (error) => {
					this.users = [];
					this.isLoading = false;
				},
			})
		);
	}

	refreshUsers() {
		this.getUsers(this.page, this.pageSize, this.term);
	}

	searchUsersByTerm(searchTerm: string) {
		this.page = 1;
		this.term = searchTerm;
		this.refreshUsers();
	}

	getRoleNameReadable(roleName: string): string {
		switch (roleName) {
			case 'ROLE_USER':
				return 'Felhasználó';
			case 'ROLE_MANAGER':
				return 'Menedzser';
			case 'ROLE_ADMIN':
				return 'Adminisztrátor';
			case 'ROLE_CHEF':
				return 'Szakács';
			default:
				return 'Ismeretlen';
		}
	}
}
