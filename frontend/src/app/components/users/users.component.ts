import { Component, OnInit } from '@angular/core';
import { UserWithGroup } from 'src/app/models/UserWithGroup';

@Component({
	selector: 'app-users',
	templateUrl: './users.component.html',
	styleUrls: ['./users.component.css'],
})
export class UsersComponent implements OnInit {
	USERS!: UserWithGroup[];

	page: number = 1;
	userCountPerPage: number = 10;
	collectionSize!: number;
	visibleUsers!: UserWithGroup[];

	constructor() {
		this.USERS = [];

		this.USERS.push(
			new UserWithGroup(
				1,
				'TesztElek15',
				'Teszt Elek Béla',
				'tesztelek15@tesztmail.com',
				2663333,
				'Szakács'
			)
		);

		this.USERS.push(
			new UserWithGroup(
				2,
				'TesztPisza3',
				'Teszt Pista',
				'tesztpista3@tesztmail.com',
				2664444,
				'Admin'
			)
		);

		this.USERS.push(
			new UserWithGroup(
				3,
				'TesztJeno',
				'Teszt Jenő',
				'tesztijenci@tesztmail.com',
				2665555,
				'Felhasználó'
			)
		);

		this.USERS.push(
			new UserWithGroup(
				4,
				'TesztManager20',
				'Teszt Manager Feri',
				'tesztmanager20@tesztmail.com',
				2666666,
				'Üzletvezető'
			)
		);

		this.collectionSize = this.USERS.length;

		this.refreshUsers();
	}

	ngOnInit(): void {}

	refreshUsers() {
		this.visibleUsers = this.USERS.slice(
			(this.page - 1) * this.userCountPerPage,
			(this.page - 1) * this.userCountPerPage + this.userCountPerPage
		);
	}
}
