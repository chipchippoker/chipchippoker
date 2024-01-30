<template>
    <div class="modal-dialog modal-dialog-centered modal-lg position-relative">
        <div class="modal-content" style="background-color: #fff0c0;">
            <div class="modal-header border-0">
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body" >
                <div id="carouselExampleIndicators" class="carousel slide d-flex justify-content-center align-items-center">
                    <div class="carousel-indicators">
                        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
                        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
                        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
                        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="3" aria-label="Slide 4"></button>
                        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="4" aria-label="Slide 5"></button>
                    </div>
                    <div class="carousel-inner guide-modal scrolled" >
                        <div class="carousel-item active">
                            <guide1 class="guide-inside"/>
                        </div>
                        <div class="carousel-item">
                            <guide2 class="guide-inside"/>
                        </div>
                        <div class="carousel-item">
                            <guide3 class="guide-inside"/>
                        </div>
                        <div class="carousel-item">
                            <guide4 class="guide-inside"/>
                        </div>
                        <div class="carousel-item">
                            <guide5 class="guide-inside"/>
                        </div>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                    </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import guide1 from '@/components/Modal/Guide/guide1.vue';
import guide2 from '@/components/Modal/Guide/guide2.vue';
import guide3 from '@/components/Modal/Guide/guide3.vue';
import guide4 from '@/components/Modal/Guide/guide4.vue';
import guide5 from '@/components/Modal/Guide/guide5.vue';

</script>

<style scoped>
.guide-modal{
    width: 75%;
    padding-bottom: 50px;
}
.guide-inside {
    border-radius: 15px;
    background-color: #fff;
    padding: 20px;
    height: 400px;
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
}
</style>