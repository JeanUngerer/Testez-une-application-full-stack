import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import {Observable, of, throwError} from 'rxjs';

import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock = {
    register: jest.fn(),
  };
  let routerMock= {
    navigate: jest.fn(),
  };

  beforeEach(async () => {
    authServiceMock = {
      register: jest.fn(),
    };

    routerMock= {
      navigate: jest.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be able to submit registration and navigate', () => {
    authServiceMock.register.mockReturnValue(of(null));
    routerMock.navigate.mockReturnValue(of(null));
    component.submit();

    expect(authServiceMock.register).toHaveBeenCalledWith({email: "", firstName: "", lastName: "", password: ""});
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should handle registration error', () => {
    const errorResponse = 'error';
    authServiceMock.register.mockReturnValue(new Observable(observer => {
      observer.error(errorResponse);
    }));
    component.submit();
    expect(routerMock.navigate).toBeCalledTimes(0);

    expect(component.onError).toBe(true);
  });
});
