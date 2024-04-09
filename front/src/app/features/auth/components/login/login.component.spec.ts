import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock = {
    login: jest.fn(),
  };
  let routerMock = {
    navigate: jest.fn(),
  };

  beforeEach(async () => {
    authServiceMock = {
      login: jest.fn(),
    };

    routerMock= {
      navigate: jest.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    })
    .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be able to submit login and navigate', () => {
    authServiceMock.login.mockReturnValue(of(null));
    routerMock.navigate.mockReturnValue(of(null));
    component.submit();

    expect(authServiceMock.login).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should handle login error', () => {
    const errorResponse = 'error';
    authServiceMock.login.mockReturnValue(throwError(errorResponse));
    component.submit();

    expect(routerMock.navigate).toBeCalledTimes(0);
    expect(component.onError).toBe(true);
  });
});
