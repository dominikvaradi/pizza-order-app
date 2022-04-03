import { Component, OnInit } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormGroup,
	Validators,
} from '@angular/forms';
import { faEye, faEyeSlash } from '@fortawesome/free-regular-svg-icons';
import { LoginUser } from 'src/app/models/LoginUser';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
	loginForm: FormGroup = this.formBuilder.group({
		username: ['', [Validators.required]],
		password: ['', [Validators.required]],
	});

	get username(): AbstractControl | null {
		return this.loginForm.get('username');
	}

	get password(): AbstractControl | null {
		return this.loginForm.get('password');
	}

	faEyeSlashIcon = faEyeSlash;
	faEyeIcon = faEye;

	isPasswordFieldHidden: boolean = true;
	isPasswordConfirmFieldHidden: boolean = true;

	constructor(private formBuilder: FormBuilder) {}

	ngOnInit(): void {}

	onSubmit() {
		const model: LoginUser = new LoginUser(
			this.username?.value,
			this.password?.value
		);

		// TODO Service lekezelés
		console.log(JSON.stringify(model));
	}
}
