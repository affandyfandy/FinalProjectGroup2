import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { AddStudioDTO, Studio } from '../../../model/studio.model';
import { StudioService } from '../../../services/studio/studio.service';

@Component({
  selector: 'app-studio',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  providers: [
    StudioService
  ],
  templateUrl: './studio.component.html',
  styleUrl: './studio.component.css'
})
export class StudioComponent implements OnInit {

  @ViewChild('studioForm') studioForm: NgForm = {} as NgForm;

  studioList: Studio[] = [];
  currentStudio: Studio = { name: '' };
  currentStudioName = '';
  tempStudioName = '';
  isEdit = false;
  currentPage = 1;
  totalPages = 1;

  constructor(
    private studioService: StudioService
  ) { }

  ngOnInit(): void {
    this.getStudioList();
  }

  getStudioList(page: number = 0) {
    this.studioService.getStudioList(page).subscribe({
      next: (res: any) => {
        this.studioList = res.content;
        this.currentPage = page;
        this.totalPages = res.totalPages;
      },
      error: (err) => {
        console.error('Failed to get studio list: ', err);
      }
    });
  }

  addStudio() {
    this.studioService.addStudio({ name: this.currentStudioName }).subscribe({
      next: () => {
        this.getStudioList(this.currentPage);
        this.closeStudioModal();
      },
      error: (err) => {
        console.error('Failed to add studio: ', err);
      }
    });
  }

  editStudio() {
    this.studioService.editStudio(this.currentStudio.id!, { name: this.currentStudioName }).subscribe({
      next: () => {
        this.getStudioList(this.currentPage);
        this.closeStudioModal();
      },
      error: (err) => {
        console.error('Failed to edit studio: ', err);
      }
    });
  }

  changeStatus(id: number) {
    this.studioService.changeStatus(id).subscribe({
      next: () => {
        this.getStudioList(this.currentPage);
      },
      error: (err) => {
        console.error('Failed to change studio status: ', err);
      }
    });
  }

  showStudioModal(isEdit: boolean, studio?: Studio) {
    this.currentStudio = studio || {};
    this.currentStudioName = studio?.name || '';
    this.tempStudioName = studio?.name || '';
    this.isEdit = isEdit;

    const modal = document.getElementById('studio-modal') as HTMLDialogElement;
    modal.showModal();
  }

  closeStudioModal() {
    this.studioForm.resetForm();
    const modal = document.getElementById('studio-modal') as HTMLDialogElement;
    this.tempStudioName = '';
    this.currentStudioName = '';
    modal.close();
  }

  onClickSave() {
    if (this.isEdit) {
      this.editStudio();
    } else {
      this.addStudio();
    }
  }
}
