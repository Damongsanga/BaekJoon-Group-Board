<template>
    <div class="p-4 mb-4 border border-yellow-600 rounded-lg shadow h-fit sm:p-6">


        <div class="grid gap-6">
            <GroupPyramid />
            <div class="p-5 border border-white rounded-lg">
                <GroupUserGrid />
            </div>

        </div>


    </div>
</template>
<script>
import GroupUserGrid from '@/components/mainpage/leftchange/pyramid-and-usergrid/GroupUserGrid.vue';
import GroupPyramid from '@/components/mainpage/leftchange/pyramid-and-usergrid/GroupPyramid.vue';
export default {
    components: { GroupUserGrid, GroupPyramid }
}
</script>

