package com.rola.app.enterprise.assignment

import com.rola.app.data.database.EnterpriseDao
import com.rola.app.data.database.entities.toEntity
import com.rola.app.domain.model.Assignment
import com.rola.app.domain.model.AssignmentSubmission
import com.rola.app.domain.model.Permission
import com.rola.app.domain.model.SubmissionStatus
import com.rola.app.enterprise.administration.RoleManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignmentManager @Inject constructor(
    private val enterpriseDao: EnterpriseDao,
    private val roleManager: RoleManager,
) {
    suspend fun createAssignment(
        institutionId: String,
        assignment: Assignment,
    ) {
        require(roleManager.hasPermission(assignment.teacherId, institutionId, Permission.CreateAssignments)) {
            "Assignment creation permission is required."
        }
        enterpriseDao.upsertAssignments(listOf(assignment.toEntity()))
        roleManager.audit(institutionId, assignment.teacherId, "create_assignment", "assignments", assignment.assignmentId)
    }

    suspend fun submitAssignment(
        institutionId: String,
        submission: AssignmentSubmission,
    ) {
        require(roleManager.hasPermission(submission.studentId, institutionId, Permission.ViewOwnProgress)) {
            "Student submission permission is required."
        }
        enterpriseDao.upsertSubmissions(listOf(submission.copy(status = SubmissionStatus.Submitted, submittedAt = System.currentTimeMillis()).toEntity()))
        roleManager.audit(institutionId, submission.studentId, "submit_assignment", "submissions", submission.submissionId)
    }

    suspend fun reviewSubmission(
        institutionId: String,
        teacherId: String,
        submission: AssignmentSubmission,
        score: Int,
        feedback: String,
    ) {
        require(roleManager.hasPermission(teacherId, institutionId, Permission.ReviewSubmissions)) {
            "Submission review permission is required."
        }
        enterpriseDao.upsertSubmissions(
            listOf(
                submission.copy(
                    status = SubmissionStatus.Reviewed,
                    score = score.coerceIn(0, 100),
                    feedback = feedback.take(500),
                ).toEntity(),
            ),
        )
        roleManager.audit(institutionId, teacherId, "review_submission", "submissions", submission.submissionId)
    }
}
