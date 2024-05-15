import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlaceAutoCompleteComponent } from './place-auto-complete.component';

describe('PlaceAutoCompleteComponent', () => {
  let component: PlaceAutoCompleteComponent;
  let fixture: ComponentFixture<PlaceAutoCompleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PlaceAutoCompleteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PlaceAutoCompleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
