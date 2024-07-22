package campus.tech.kakao.map.adapter

import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.SearchWord

interface AdapterCallback {
	fun onWordAdded(document: Document)
	fun onDocumentInfoSet(document: Document)
	fun onWordDeleted(searchWord: SearchWord)
	fun onWordSearched(searchWord: SearchWord)
}