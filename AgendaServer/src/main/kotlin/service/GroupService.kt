package service

import model.*
import org.jetbrains.exposed.sql.*
import service.DatabaseFactory.dbQuery

class GroupService {

    suspend fun getAllGroups(): List<Group> = dbQuery {
        TB_Group.selectAll().map { toGroup(it) }
    }

    suspend fun getGroup(id: Int): Group? = dbQuery {
        TB_Group.select {
            (TB_Group.id_group eq id)
        }.mapNotNull { toGroup(it) }
                .singleOrNull()
    }

    suspend fun updateGroup(groupUpdated: NewGroup): Group {
        val id = groupUpdated.id_group
        return if (id == null) {
            addGroup(groupUpdated)
        } else {
            dbQuery {
                TB_Group.update({ TB_Group.id_group eq id }) {
                    it[group_title] = groupUpdated.group_title
                    it[group_description] = groupUpdated.group_description
                }
            }
            getGroup(id)!!
        }
    }

    suspend fun addGroup(newGroup: NewGroup): Group {
        var key: Int? = 0
        dbQuery {
            key = model.TB_Group.insert({
                it[group_title] = newGroup.group_title
                it[group_description] = newGroup.group_description
            }) get TB_Group.id_group
        }
        return getGroup(key!!)!!
    }

    suspend fun deleteGroup(id: Int): Boolean = dbQuery {
        TB_Group.deleteWhere { TB_Group.id_group eq id } > 0
    }

    private fun toGroup(row: ResultRow): Group =
            Group(
                    id_group = row[TB_Group.id_group],
                    group_title = row[TB_Group.group_title],
                    group_description = row[TB_Group.group_description]
            )
}
