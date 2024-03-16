<template>
    <div @click="handleClick" class="overflow-hidden rounded-md w-7 h-7">
        <img :src="src" :alt="alt" class="object-cover w-full h-full rounded-md" />
        <!-- 여기에 타일의 다른 내용 추가 -->
    </div>
</template>
  
<script>
export default {
    props: {
        src: String,  // 이미지 URL
        alt: String,// 대체 텍스트
    },
    methods: {
        handleClick() {
            this.$emit('onTileClick', this.userId);
        }
    }
}
</script>
  