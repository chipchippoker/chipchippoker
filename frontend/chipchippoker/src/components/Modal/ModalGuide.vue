<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content" style="background-color: #ffde76;">
            <div class="modal-header border-0">
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div>
                    <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="true">
                        <div class="carousel-indicators">
                            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0"
                                class="active" aria-current="true" aria-label="Slide 1"></button>
                            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1"
                                aria-label="Slide 2"></button>
                            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2"
                                aria-label="Slide 3"></button>
                            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="3"
                                aria-label="Slide 4"></button>
                            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="4"
                                aria-label="Slide 5"></button>
                        </div>
                        <div class="carousel-inner p-3" style="height: 300px;">
                            <div class="carousel-item bg-white p-3 rounded-3 active">
                                <h1>게임 초기 세팅</h1>
                                <div>
                                    카드 세트 : 인원 수만큼의 카드세트가 주어집니다
                                    칩 : 한 사람 당 25개의 칩이 주어집니다
                                    인원 수 : 2인에서 4인까지 가능합니다
                                    시간 제한 : 한사람당 15초의 배팅 시간이 주어집니다
                                </div>
                            </div>
                            <div class="carousel-item bg-white p-3 rounded-3">
                                <h1>게임 룰</h1>
                                <div>
                                    플레이시 플레이어가 가능한 행동은 두 가지 입니다.
                                    배팅
                                    폴드
                                </div>
                            </div>
                            <div class="carousel-item bg-white p-3 rounded-3">
                                <h1>배팅</h1>
                                <div>
                                    배팅을 할 때 두가지 행동이 존재합니다
                                    상대방의 배팅 금액에 맞춰 배팅하기(콜)
                                    모든 참가자들의 배팅 금액이 같아질 경우
                                    모두 카드 패를 확인하고 가장 높은 숫자의 사람이 배팅 코인을 모두 가져가게 됩니다
                                    상대보다 높은 금액을 배팅하기(레이즈)
                                    상대보다 높은 금액을 배팅하여 추가 배팅을 진행합니다
                                </div>

                            </div>
                            <div class="carousel-item bg-white p-3 rounded-3">
                                <h1>폴드</h1>
                                <div>
                                    상대방의 코인 배팅을 따라가기 힘들거나
                                    상대방의 카드보다 자신의 카드가 낮을 것 같다고 생각될 때
                                    폴드를 진행할 수 있습니다
                                    폴드를 하면 현재 배팅한 금액을 모두 상대방에게 빼앗기게 됩니다.
                                    추가로, 본인이 10을 가지고 있었다면 10개의 코인을 더 빼앗기게 됩니다.

                                    ※ TIP! 상대방의 카드가 높더라도 폴드를 진행하지 않고 배팅하여
                                    심리적으로 상대방의 폴드를 이끌어 낼 수도 있습니다.
                                </div>

                            </div>
                            <div class="carousel-item bg-white p-3 rounded-3">
                                <h1>카드 서열</h1>
                                <div>
                                    A는 1로 취급됩니다.
                                    카드는 1 ~ 10 까지 존재하며,
                                    기본적으로 높은 숫자가 높은 서열을 가지고 있습니다
                                    하지만 A는 10보다 높은 서열을 가집니다

                                    ※ TIP! 상대방이 A라고 방심하지 마세요!, 내가 10일수도 있거든요
                                </div>

                            </div>
                        </div>
                        <button class="carousel-control-prev yellow" type="button"
                            data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Previous</span>
                        </button>
                        <button class="carousel-control-next yellow" type="button"
                            data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Next</span>
                        </button>
                    </div>
                </div>

            </div>
        </div>
    </div>
</template>

<script setup>

</script>

<style scoped></style>