<script setup lnag ="ts">
import {ref} from "vue";

const count = ref(0)

</script>

<template>
 <!-- <p>안녕하세요</p>
 <button @click="count +=1">카운트 늘리기</button><br>
 <p>{{count}}</p>
  -->
 <div>
    <input type="text" placeholder="제목을 입력해주세요"/>
 </div>
<div>
    <textarea rows="15"></textarea>
</div>
<div>글 작성 완료</div>
</template>

<style>

</style>