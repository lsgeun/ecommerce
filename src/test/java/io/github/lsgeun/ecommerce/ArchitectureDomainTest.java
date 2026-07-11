package io.github.lsgeun.ecommerce;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.assignableTo;
import static com.tngtech.archunit.base.DescribedPredicate.not;

class ArchitectureDomainTest {

    @Test
    void 컨트롤러_계층은_도메인_계층을_직접_참조할_수_없다_단_공통_이넘은_허용() { // 💡 목적과 이름을 명확하게 일치!
        JavaClasses importedClasses = new ClassFileImporter().importPackages("io.github.lsgeun.ecommerce");

        ArchRule rule = noClasses()
            .that().resideInAPackage("..controller..")
            .should().dependOnClassesThat(
                resideInAPackage("..domain..")
                    .and(not(assignableTo(Enum.class)))
            )
            .because("컨트롤러는 도메인 엔티티나 비즈니스 로직을 직접 알면 안 되고, 서비스 계층을 거쳐야 합니다.");

        rule.check(importedClasses);
    }
}
