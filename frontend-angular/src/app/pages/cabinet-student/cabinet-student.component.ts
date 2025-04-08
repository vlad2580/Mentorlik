import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cabinet-student',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cabinet-student.component.html',
  styleUrl: './cabinet-student.component.css',
  styles: [`
    .sidebar {
      width: 250px;
      height: 100vh;
      background-color: #fff;
      position: fixed;
      left: 0;
      top: 0;
      box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
      padding: 20px;
    }

    .main-content {
      margin-left: 270px;
      padding: 20px;
    }

    .nav-list {
      list-style: none;
      padding: 0;
      margin-top: 30px;
    }

    .nav-list li {
      margin-bottom: 15px;
    }

    .nav-list a {
      text-decoration: none;
      color: #333;
      display: block;
      padding: 10px;
      border-radius: 5px;
      transition: background-color 0.3s;
    }

    .nav-list a:hover {
      background-color: #f5f5f5;
    }

    .sign-out {
      position: absolute;
      bottom: 20px;
      width: calc(100% - 40px);
    }

    .sign-out a {
      color: #ff6f3d;
      text-decoration: none;
    }

    .main-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 30px;
    }

    .main-header input {
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 5px;
      width: 300px;
    }

    .user-info {
      display: flex;
      gap: 20px;
      margin-bottom: 30px;
    }

    .user-card {
      flex: 0 0 30%;
      background-color: #fff;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
      text-align: center;
    }

    .user-photo {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      object-fit: cover;
      margin-bottom: 15px;
    }

    .user-stats {
      flex: 1;
      display: flex;
      gap: 20px;
    }

    .stat {
      flex: 1;
      background-color: #fff;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
      text-align: center;
    }

    .stat h4 {
      color: #777;
      margin-bottom: 10px;
      font-weight: normal;
    }

    .stat p {
      font-size: 24px;
      font-weight: bold;
      color: #333;
      margin: 0;
    }

    .edit-btn {
      display: inline-block;
      padding: 8px 15px;
      background-color: #ff6f3d;
      color: #fff;
      text-decoration: none;
      border-radius: 5px;
      margin-top: 15px;
      border: none;
      cursor: pointer;
    }

    .user-activity {
      background-color: #fff;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }

    .table-container {
      overflow-x: auto;
    }

    table {
      width: 100%;
      border-collapse: collapse;
    }

    th, td {
      padding: 15px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }

    th {
      background-color: #f5f5f5;
      font-weight: 500;
    }

    .status-low {
      color: #dc3545;
      font-weight: 500;
    }

    .status-mid {
      color: #fd7e14;
      font-weight: 500;
    }

    .status-high {
      color: #28a745;
      font-weight: 500;
    }

    .progress {
      width: 100%;
      height: 8px;
      background-color: #e9ecef;
      border-radius: 4px;
      overflow: hidden;
    }

    .progress span {
      display: block;
      height: 100%;
      background-color: #ff6f3d;
    }

    .delete-btn {
      background: none;
      border: none;
      cursor: pointer;
      margin-left: 5px;
    }

    footer {
      margin-top: 50px;
      text-align: center;
      color: #777;
      padding: 20px;
      margin-left: 250px;
    }
  `]
})
export class CabinetStudentComponent {

}
