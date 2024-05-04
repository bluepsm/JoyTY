import { AbstractControl, AsyncValidatorFn, ValidatorFn } from "@angular/forms";
import { AuthService } from "../services/auth.service";
import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class ValidationService {
    isUsernameExists: any;

    constructor(private authService: AuthService) {}

    match(controlName: string, checkControlName: string): ValidatorFn {
        return (controls: AbstractControl) => {
            const control = controls.get(controlName);
            const checkControl = controls.get(checkControlName);

            if (checkControl?.errors && !checkControl.errors['matching']) {
                return null;
            }

            if (control?.value !== checkControl?.value) {
                controls.get(checkControlName)?.setErrors({ matching: true });
                return { matching: true };
            } else {
                return null;
            }
        };
    }

    existsByUsername(): ValidatorFn {
        return (control: AbstractControl) => {
            this.authService.existsByUsername(control?.value).subscribe({
                next: data => {
                    if (data === true) {
                        //console.log("in checkifuserexists method: exits");
                        control.setErrors({ 'isExists': true });
                    }
                }, error: err => {
                    console.log(err);
                }
            })
            return null;
        };
    }

    existsByEmail(): ValidatorFn {
        return (control: AbstractControl) => {
            this.authService.existsByEmail(control?.value).subscribe({
                next: data => {
                    if (data === true) {
                        //console.log("in checkifuserexists method: exits");
                        control.setErrors({ 'isExists': true });
                    }
                }, error: err => {
                    console.log(err);
                }
            })
            return null;
        };
    }
}