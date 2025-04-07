//package com.dhug.example.data.pagingsource
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//
///**
// * @Author: NGUYEN XUAN DIEU
// * @Date: 21 / 12 / 2024
// */
//class MediaPagingSource(
//    private val mediaDao: MediaDao,
//    private val initialId: Long,
//    private val filter: MediaFilter
//) : PagingSource<Long, MediaItem>() {
//
//    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, MediaItem> {
//        return try {
//            val startId = params.key ?: initialId
//            val loadSize = params.loadSize
//
//            val data = when {
//                startId == initialId -> {
//                    // Lần đầu tiên, lấy cả phần trước và sau
//                    val current = mediaDao.getMediaById(initialId)
//                    val newerMedia = mediaDao.getPreviousMedia(initialId, loadSize, filter.isLock, filter.type).reversed()
//                    val olderMedia = mediaDao.getNextMedia(initialId, loadSize, filter.isLock, filter.type)
//                    newerMedia + listOfNotNull(current) + olderMedia
//                }
//                startId < initialId -> {
//                    // Tải media mới hơn (ID lớn hơn startId)
//                    mediaDao.getPreviousMedia(startId, loadSize, filter.isLock, filter.type).reversed()
//                }
//                else -> {
//                    // Tải media cũ hơn (ID nhỏ hơn startId)
//                    mediaDao.getNextMedia(startId, loadSize, filter.isLock, filter.type)
//                }
//            }
//
//            // Xác định prevKey và nextKey
//            val firstId = data.firstOrNull()?.id
//            val lastId = data.lastOrNull()?.id
//
//            LoadResult.Page(
//                data = data,
//                prevKey = if (firstId != null && data.isNotEmpty() && firstId < startId) firstId else null,
//                nextKey = if (lastId != null && data.isNotEmpty() && lastId > startId) lastId else null
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Long, MediaItem>): Long? {
//        // Lấy `id` gần nhất với vị trí trung tâm để làm mới
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.id
//        }
//    }
//}
