import { Component, Input, OnInit } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormControl,
	FormGroup,
	ValidatorFn,
	Validators,
} from '@angular/forms';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import {
	faEye,
	faEyeSlash,
	IconDefinition,
} from '@fortawesome/free-regular-svg-icons';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import CustomValidation from 'src/app/validation/custom-validation';
import CustomValidators from 'src/app/validation/validators';

@Component({
	selector: 'app-save-user-modal',
	templateUrl: './save-user-modal.component.html',
	styleUrls: ['./save-user-modal.component.css'],
})
export class SaveUserModalComponent implements OnInit {
	@Input()
	defaultValue!: String;

	@Input()
	label!: string;

	@Input()
	validators: ValidatorFn[] = [];

	@Input()
	isSecret: boolean = false;

	@Input()
	prefix: string | undefined;

	@Input()
	isNeedConfirm: boolean = false;

	editUserForm: FormGroup = this.formBuilder.group({
		value: '',
	});

	get valueControl(): AbstractControl | null {
		return this.editUserForm.get('value');
	}

	get valueConfirmControl(): AbstractControl | null {
		return this.editUserForm.get('valueConfirm');
	}

	constructor(
		private activeModal: NgbActiveModal,
		private formBuilder: FormBuilder,
		private iconLibrary: FaIconLibrary
	) {
		iconLibrary.addIcons(faEyeSlash, faEye);
	}

	ngOnInit() {
		this.valueControl?.setValue(this.defaultValue);
		this.valueControl?.addValidators(this.validators);

		if (this.isNeedConfirm) {
			this.editUserForm.addControl('valueConfirm', new FormControl('', Validators.required));
			this.valueConfirmControl?.addValidators(this.validators);
			this.editUserForm.addValidators(CustomValidation.match('value', 'valueConfirm'))
		}
	}

	onSubmit() {
		this.activeModal.close(this.valueControl?.value);
	}

	closeModal() {
		this.activeModal.dismiss('Close click');
	}
}
