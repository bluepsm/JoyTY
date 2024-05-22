import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentModalComponent } from './comment-modal.component';

describe('CommentComponent', () => {
  let component: CommentModalComponent;
  let fixture: ComponentFixture<CommentModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommentModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CommentModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
