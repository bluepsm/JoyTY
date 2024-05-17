import { Injectable, TemplateRef } from '@angular/core';

@Injectable({
  	providedIn: 'root'
})
export class ToastService {
	toasts: any[] = [];

	show(textOrTemplate: String | TemplateRef<any>, options: any = {}) {
		this.toasts.push({ textOrTemplate, ...options });
	}

	remove(toast: any) {
		this.toasts = this.toasts.filter((t) => t !== toast);
	}

	clear() {
		this.toasts.splice(0, this.toasts.length);
	}

	showStatusToast(message: String) {
		this.show(message, { classname: 'bg-dark text-light', delay: 5000 })
	}
	
	showErrorToast(message: String) {
		this.show(message, { classname: 'bg-danger text-light', delay: 15000 })
	}
}
