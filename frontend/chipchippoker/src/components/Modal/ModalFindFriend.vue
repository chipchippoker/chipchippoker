<template>
    <div class="modal-dialog ">
        <div class="modal-content " style="background-color: #ffde76;">
        <div class="modal-header border-0">
            <h1 class="modal-title fs-5" id="FindFriendModalLabel">Modal title</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body d-flex justify-content-center">
            <form class="row g-3">
                <div class="col-auto">
                    <div for="staticEmail2" class="visually-hidden">닉네임</div>
                    
                </div>
                <div class="col-auto">
                    <label for="userId" class="visually-hidden">nickname</label>
                    <input type="text" class="form-control" id="userId" placeholder="Nickname">
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary mb-3">검색</button>
                </div>
            </form>



        </div>
        <div class="text-center mb-3 ">
            검색 결과 없음
        </div>
        
        </div>
    </div>
</template>

<script setup>

</script>

<style lang="scss" scoped>

</style>