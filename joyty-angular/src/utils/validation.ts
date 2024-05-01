import { OnInit } from "@angular/core";
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";

export class AppComponent implements OnInit {
    dobForm: FormGroup = new FormGroup({
        dateOfBirth: new FormControl('')
    });

    constructor(private formBuilder: FormBuilder) {}

    ngOnInit(): void {
        this.dobForm = this.formBuilder.group({
            dateOfBirth: ['', Validators.required]
        })
    }

    get df(): { [key: string]: AbstractControl } {
        return this.dobForm.controls;
    }
}