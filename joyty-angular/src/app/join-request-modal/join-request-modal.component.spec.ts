import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinRequestModalComponent } from './join-request-modal.component';

describe('JoinRequestModalComponent', () => {
  let component: JoinRequestModalComponent;
  let fixture: ComponentFixture<JoinRequestModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [JoinRequestModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(JoinRequestModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
