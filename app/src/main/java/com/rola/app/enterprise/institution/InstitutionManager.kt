package com.rola.app.enterprise.institution

import com.rola.app.data.database.EnterpriseDao
import com.rola.app.data.database.entities.toEntity
import com.rola.app.domain.model.ClassGroup
import com.rola.app.domain.model.Course
import com.rola.app.domain.model.Department
import com.rola.app.domain.model.InstitutionMember
import com.rola.app.domain.model.InstitutionProfile
import com.rola.app.domain.model.Permission
import com.rola.app.enterprise.administration.RoleManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstitutionManager @Inject constructor(
    private val enterpriseDao: EnterpriseDao,
    private val roleManager: RoleManager,
) {
    suspend fun upsertInstitution(actorUserId: String, profile: InstitutionProfile) {
        requireAdmin(actorUserId, profile.institutionId)
        enterpriseDao.upsertInstitutions(listOf(profile.toEntity()))
        roleManager.audit(profile.institutionId, actorUserId, "upsert_institution", "institutions", profile.institutionId)
    }

    suspend fun addDepartment(actorUserId: String, department: Department) {
        requireAdmin(actorUserId, department.institutionId)
        enterpriseDao.upsertDepartments(listOf(department.toEntity()))
        roleManager.audit(department.institutionId, actorUserId, "upsert_department", "departments", department.departmentId)
    }

    suspend fun addCourse(actorUserId: String, course: Course) {
        requireAdmin(actorUserId, course.institutionId)
        enterpriseDao.upsertCourses(listOf(course.toEntity()))
        roleManager.audit(course.institutionId, actorUserId, "upsert_course", "courses", course.courseId)
    }

    suspend fun addClass(actorUserId: String, classGroup: ClassGroup) {
        val allowed = roleManager.hasPermission(actorUserId, classGroup.institutionId, Permission.ManageClasses)
        require(allowed) { "Class management permission is required." }
        enterpriseDao.upsertClasses(listOf(classGroup.toEntity()))
        roleManager.audit(classGroup.institutionId, actorUserId, "upsert_class", "classes", classGroup.classId)
    }

    suspend fun addMember(actorUserId: String, member: InstitutionMember) {
        val permission = if (member.role == com.rola.app.domain.model.UserRole.Student) {
            Permission.ManageStudents
        } else {
            Permission.ManageTeachers
        }
        require(roleManager.hasPermission(actorUserId, member.institutionId, permission)) {
            "Member management permission is required."
        }
        enterpriseDao.upsertMembers(listOf(member.toEntity()))
        roleManager.audit(member.institutionId, actorUserId, "upsert_member", "members", member.memberId)
    }

    private suspend fun requireAdmin(actorUserId: String, institutionId: String) {
        require(roleManager.hasPermission(actorUserId, institutionId, Permission.ManageInstitution)) {
            "Institution administration permission is required."
        }
    }
}
