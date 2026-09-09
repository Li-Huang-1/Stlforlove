<template>
  <ArticleList 
    type="tech" 
    page-title="技术栈" 
    page-subtitle="代码世界里的探索与思考" 
  />
</template>

<script>
import ArticleList from './ArticleList.vue'

export default {
  name: 'TechArticleList',
  components: { ArticleList }
}
</script>
