import { Component, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DataSourceService } from '../Service/data-source.service';
import { Router } from '@angular/router';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { AuthService } from '../AuthService.service (1)';
import { DataSource } from '../Service/datasource';

@Component({
  selector: 'app-loginservice',
  templateUrl: './loginservice.component.html',
  styleUrls: ['./loginservice.component.css'], // Change styleUrl to styleUrls
  encapsulation: ViewEncapsulation.None
})
export class LoginserviceComponent {


  constructor(private fb: FormBuilder,
     private service: DataSource, private router: Router, private snackBar: MatSnackBar,
    private authService:AuthService) { }

  public showPassword:boolean=false;
  public isLoading=false;
  public loginForm = this.fb.group({
    email: this.fb.control('', [Validators.required, Validators.email]),
    password: this.fb.control('', [Validators.required, Validators.minLength(8)])
  })

  private showSnackBar(message: string) {
    const config = new MatSnackBarConfig();
    config.panelClass = ['custom-snackbar']; // Add your custom class for styling
    config.duration = 2000;
    config.verticalPosition = 'top';
    this.snackBar.open(message, 'close', config);
  }

  togglePasswordVisibility(event: MouseEvent): void {
    event.preventDefault();
    this.showPassword = !this.showPassword;
  }

  loginData() {
   this.isLoading=true;
    if (this.loginForm.value.email !== null && this.loginForm.value.email !== undefined &&
      this.loginForm.value.password !== null && this.loginForm.value.password !== undefined) {
      // alert(this.loginForm.value)
      this.service.validateUser(this.loginForm.value.email, this.loginForm.value.password).subscribe((data: any) => {
        this.isLoading=false;
        // alert(JSON.stringify(data.userRole))
        if(data.userRole=="User" && data.userStatus=="Active"){
          this.showSnackBar("Login successFully")
          this.authService.loginin(data)
          this.router.navigate(['/medicine']);
        }
        else if(data.userRole=="admin" && data.userStatus=="Active"){
          this.showSnackBar("Login successFully")
          this.authService.loginin(data)
          this.router.navigate(['/admin/AddProduct']);
        }
        else{
          this.showSnackBar("Please Signup to register")
        }
      },
        (error: any) => {
          this.isLoading=false;
          // alert("Error occurred : " + JSON.stringify(error));
          this.showSnackBar("Login failed. Please enter valid credentials")
        })
    }
    else {
      this.isLoading=false;
      alert("Login credentails are invalid. Please enter valid credentials");
    }
  }

}
