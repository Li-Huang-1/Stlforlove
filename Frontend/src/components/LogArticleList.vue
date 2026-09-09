<template>
  <ArticleList 
    type="log" 
    page-title="日志" 
    page-subtitle="记录博客成长的每一步" 
  />
</template>

<script>
import ArticleList from './ArticleList.vue'

export default {
  name: 'LogArticleList',
  components: { ArticleList }
}
</script>
